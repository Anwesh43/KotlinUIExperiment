package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class RotatorDotLineView(ctx:Context):View(ctx) {
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
    data class RotatorLine(var x:Float,var y:Float,var deg:Float,var size:Float) {
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            canvas.drawLine(0f,0f,0f,-size,paint)
            canvas.restore()
        }
    }
}