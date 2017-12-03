package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 03/12/17.
 */
import android.graphics.*
import android.content.*
import android.view.*

class ConcentricCircleIndicatorView(ctx:Context):View(ctx) {
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
    data class ConcentricCircle(var r:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f,false,paint)
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}