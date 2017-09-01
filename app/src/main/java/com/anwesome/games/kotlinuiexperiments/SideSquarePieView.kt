package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 02/09/17.
 */
class SideSquarePieView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class SideSquarePie(var w:Float,var h:Float,var i:Int) {
        var r = Math.min(w,h)/15
        var x = (w/2-2*r)+(i%2)*4*r
        var y = (h/2 -2*r)+(i/2)*4*r
        var state = SideSquareState()
        fun draw(canvas:Canvas,paint:Paint) {
            var rx = Math.min(w,h)/12
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.save()
            canvas.rotate(90f*i)
            paint.style = Paint.Style.STROKE
            canvas.drawRoundRect(RectF(-2*w/5,-2*h/5,2*w/5,-2*h/5+h/10),rx,rx,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.translate(-2*w/5,-2*h/5+h/20)
            canvas.scale(state.scale,1f)
            canvas.drawRoundRect(RectF(0f,-h/20,4*w/5,h/20),rx,rx,paint)
            canvas.restore()
            canvas.restore()
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(x-w/2,y-h/2,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x - r && x<=this.x+r && y>=this.y-r && y<=this.y+r
        fun update() {
            state.update()
        }
        fun startUpdate() {
            state.startUpdate()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class SideSquareState(var scale:Float = 0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0
            }
            if(scale < 0) {
                scale = 0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun startUpdate() {
            dir = (1 - 2 * scale).toInt()
        }
    }
}