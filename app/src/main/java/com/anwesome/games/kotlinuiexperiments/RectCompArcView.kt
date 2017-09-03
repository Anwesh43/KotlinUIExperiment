package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 04/09/17.
 */
class RectCompArcView(ctx:Context):View(ctx) {
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
    data class RectCompArc(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w / 2, h / 2)
                canvas.scale(1f-2*i,1f)
                canvas.drawArc(RectF(-0.45f*w,-0.05f*w,-0.35f*w,0.05f*w),0f,360f,true,paint)
                canvas.drawRect(RectF(-0.35f*w,-0.05f*w,-0.35f*w+0.35f*w,0.05f*w),paint)
                canvas.restore()
            }
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
    }
}