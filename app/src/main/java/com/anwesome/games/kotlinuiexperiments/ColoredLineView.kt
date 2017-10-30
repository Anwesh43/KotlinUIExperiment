package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/10/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class ColoredLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class ColoredLine(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            val adjustedSize = size/4+(size/4)
            paint.strokeWidth = size/20
            canvas.save()
            canvas.translate(x,y)
            canvas.drawLine(0f,-adjustedSize,0f,adjustedSize,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}