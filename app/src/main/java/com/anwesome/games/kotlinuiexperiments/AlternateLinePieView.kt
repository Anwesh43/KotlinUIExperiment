package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class AlternateLinePieView(ctx:Context):View(ctx) {
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
    data class AlternateLine(var i:Int,var x:Float,var y:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#00E676")
            canvas.save()
            canvas.translate(x,y)
            when(i%2) {
                0 -> canvas.drawLine(0f,0f,0f,h*scale,paint)
                1 -> canvas.drawLine(0f,h,0f,h*(1-scale),paint)
            }
            canvas.restore()
        }
    }
}