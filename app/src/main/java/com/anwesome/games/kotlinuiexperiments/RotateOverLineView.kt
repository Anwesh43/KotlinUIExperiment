package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 05/12/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class RotateOverLineView(ctx:Context):View(ctx) {
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
    data class RotateOverLine(var x:Float,var y:Float,var size:Float,var deg:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,size/10,paint)
            canvas.save()
            canvas.rotate(deg)
            canvas.drawLine(0f,0f,size,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            deg += 15f
        }
    }
}