package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 21/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
class CompletionIndicatorView(ctx:Context,var n:Int):View(ctx) {
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
    data class CompletionIndicator(var x:Float,var y:Float,var r:Float,var gap:Float,var deg:Float = 0f,var prevDeg:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.color = Color.GRAY
            canvas.drawCircle(x,y,r,paint)
            paint.color = Color.CYAN
            canvas.drawArc(RectF(x-r,y-r,x+r,y+r),0f,deg,false,paint)
        }
        fun update(scale:Float) {
            deg = prevDeg+gap*scale
        }
        fun setDeg() {
            deg = prevDeg+gap
            prevDeg = deg
        }
    }
    data class LineIndicator(var x:Float,var y:Float,var w:Float,var k:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.CYAN
            paint.strokeWidth = w/35
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(x,y,x+k,y,paint)
        }
        fun update(scale:Float) {
            k = w*scale
        }
    }
}