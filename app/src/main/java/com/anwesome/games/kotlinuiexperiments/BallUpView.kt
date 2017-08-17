package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 17/08/17.
 */
class BallUpView(ctx:Context):View(ctx) {
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
    data class BallUp(var x:Float,var y:Float,var r:Float,var h:Float) {
        var origY = 0.0f
        init {
            origY = y
        }

        fun draw(canvas: Canvas, paint: Paint) {
            canvas.save()
            canvas.translate(x, y)
            paint.color = Color.parseColor("#ef5350")
            canvas.drawCircle(0.0f, 0.0f, r, paint)
            canvas.restore()
        }

        fun update(scale: Float) {
            y = origY - h * scale
        }
        fun handleTap(x: Float, y: Float): Boolean = x >= this.x - r && x <= this.x + r && y >= this.y - r && y <= this.y + r
    }
}