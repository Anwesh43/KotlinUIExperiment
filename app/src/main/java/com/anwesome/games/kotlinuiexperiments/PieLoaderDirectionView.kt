package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 18/09/17.
 */
class PieLoaderDirectionView(ctx:Context):View(ctx) {
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
    data class PieDirectionLoader(var w:Float,var h:Float) {
        fun draw(canvas: Canvas,paint:Paint) {
            val color = Color.parseColor("00ACC1")
            paint.color = color
            canvas.save()
            canvas.translate(w / 2, h / 2)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f, 0f, w / 5, paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w / 5, -w / 5, w / 5, w / 5), 0f, 360f, true, paint)
            canvas.restore()
            canvas.drawLine(-w / 20, 4 * h / 5, -w / 20 + (w), 4 * h / 5, paint)
            canvas.drawCircle(-w / 20 + (w), 4 * h / 5, w / 40, paint)
            paint.color = Color.argb(150, Color.red(color), Color.green(color), Color.blue(color))
            canvas.drawCircle(-w/20+(w),4*h/5,w/30,paint)
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-w/10 && x<=w/2+w/10 && y>=h/2-w/10 && y<=h/2+w/10
    }
}