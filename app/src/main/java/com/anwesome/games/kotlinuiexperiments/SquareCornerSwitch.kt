package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 11/08/17.
 */
class SquareCornerSwitch(ctx:Context):View(ctx) {
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
    data class Corner(var x:Float,var y:Float,var r:Float,var state:CornerState= Corner.CornerState()) {
        fun draw(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.scale(state.scale,state.scale)
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating(dir:Int) {
            state.dir = dir
        }
        fun stopped():Boolean = state.dir == 0
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
        class CornerState(var scale:Float=0.0f,var dir:Int=0) {
            fun update() {
                scale+=0.2f*dir
                if(scale > 1) {
                    scale = 1.0f
                    dir = 0
                }
                if(scale < 0) {
                    scale = 0.0f
                    dir = 0
                }
            }
        }
    }
    class CornerSquare(var w:Float,var h:Float) {
        var corners:ConcurrentLinkedQueue<Corner> = ConcurrentLinkedQueue()
        init {
            var deg = -45

            for(i in 0..3) {
                var r = Math.min(w,h)/2
                var x = w/2 + r*Math.cos(deg*Math.PI/180).toFloat()
                var y = h/2 + r*Math.sin(deg*Math.PI/180).toFloat()
                corners.add(Corner(x,y,r/5))
                deg+=90
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.BLUE
            var r = Math.min(w,h)/2
            paint.strokeWidth = r/20
            for(i in 0..3) {
                var r = Math.min(w,h)/2
                canvas.save()
                canvas.translate(w/2,h/2)
                canvas.rotate(i*90.0f)
                canvas.drawLine(w/2,-(h/2-r),w/2,(h/2-r),paint)
                canvas.restore()
            }
            corners.forEach { corner ->
                corner.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float):Corner? {
            corners.forEach { corner->
                if(corner.handleTap(x,y)) {
                    return corner
                }
            }
            return null
        }
    }
}