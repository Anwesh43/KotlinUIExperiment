package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 01/10/17.
 */
class ClockTapView(ctx:Context):View(ctx) {
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
    data class TapClock(var w:Float,var h:Float,var size:Float = 2*Math.min(w,h)/5,var hDeg:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = size/60
            canvas.drawCircle(0f,0f,size,paint)
            paint.strokeWidth = size/50
            drawMinuteHand(canvas,paint)
            paint.strokeWidth = size/35
            drawHourHand(canvas,paint)
            canvas.restore()
        }
        private fun drawMinuteHand(canvas: Canvas,paint: Paint) {
            canvas.save()
            canvas.rotate(360f)
            canvas.drawLine(0f,0f,0f,-2*size/3,paint)
            canvas.restore()
        }
        private fun drawHourHand(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.rotate(hDeg+5f)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-size && x<=w/2+size && y>=h/2-size && y<=h/2+size
    }
}