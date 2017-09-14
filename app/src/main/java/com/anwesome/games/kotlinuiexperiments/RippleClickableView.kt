package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 14/09/17.
 */
class RippleClickableView(ctx:Context):View(ctx) {
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
    data class RippleClickable(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            canvas.drawRoundRect(RectF(-r/10,-r/10,r/10,r/10),r/40,r/40,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = true
    }
    data class RippleClickableState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 4.5f
            scale = Math.floor(Math.sin(deg*Math.PI/180)).toFloat()
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
}