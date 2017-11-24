package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 24/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
class UpDownBallView(ctx:Context):View(ctx) {
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
    data class UpDownBall(var x:Float,var y:Float,var r:Float,var h:Float,var py:Float = y) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            y = py + h*scale
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
        }
    }
}