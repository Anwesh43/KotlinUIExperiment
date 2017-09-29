package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 30/09/17.
 */
class DirectionColoredArrowView(ctx:Context):View(ctx) {
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
    data class DirectionColoredArrow(var w:Float,var h:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            val path = Path()
            path.addRect(RectF(0f,0f,w,h),Path.Direction.CW)
            canvas.drawLine(w/2,h-h/20,w/2,h/20,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w/2,h/20)
                canvas.scale(1f-2*i,1f)
                canvas.drawLine(0f,0f,-w/3,h/5,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
    }
}