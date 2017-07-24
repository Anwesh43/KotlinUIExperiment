package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 24/07/17.
 */
class PieSwitchListView(ctx:Context,var n:Int=5):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class PSVRenderer  {
        var time = 0
        fun render(canvas:Canvas,paint: Paint,v:PieSwitchListView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    data class PieSwitch(var x:Float,var y:Float,var r:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            canvas.restore()
        }
        fun update(scale:Float) {
            deg = 360.0f*scale
        }
        fun handleTap(x:Float,y:Float):Boolean =  x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class AnimationHandler(var w:Float,var h:Float,var v:PieSwitchListView) {
        var prev:PieSwitch?=null
        var curr:PieSwitch?=null
        var pieSwitches:ConcurrentLinkedQueue<PieSwitch> = ConcurrentLinkedQueue()
        init {
            var size = w/(2*v.n+1)
            var x = 3*size/2
            var y = h/2
            for(i in 1..v.n) {
                pieSwitches.add(PieSwitch(x,y,size/2))
                x += 2*size
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            pieSwitches.forEach {
                it.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float) {
            pieSwitches.forEach {
                if(it.handleTap(x,y)) {
                    curr = it
                }
            }
        }
    }
    class StateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale+=0.2f*dir
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
        }
        fun startUpdating() {
            if(dir == 0) {
                scale = 0.0f
                dir = 1
            }
        }
        fun stopped():Boolean = dir == 0
    }
}