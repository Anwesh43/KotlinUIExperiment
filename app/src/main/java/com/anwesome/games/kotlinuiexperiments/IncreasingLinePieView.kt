package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 04/12/17.
 */
import android.graphics.*
import android.view.*
import android.content.*

class IncreasingLinePieView(ctx:Context):View(ctx) {
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
    class IncreasingLine(var x:Float,var y:Float,var w:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawLine(0f,0f,w,0f,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(stopcb:()->Unit) {

        }
    }
}