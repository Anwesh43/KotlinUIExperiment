package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 22/11/17.
 */

class ArcLineMoverView(ctx:Context):View(ctx) {
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
    data class ArcLineMover(var w:Float,var h:Float) {
        var state = ArcLineMoverState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = Math.min(w,h)/40
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.save()
            canvas.rotate(90f)
            canvas.drawLine(0f,0f,w/3,0f,paint)
            canvas.restore()
            canvas.drawArc(RectF(-w/3,-w/3,w/3,w/3),0f,90f,false,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()

    }
    data class ArcLineMoverState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 10f
            if(deg > 180) {
                deg = 0f
            }
            scale = Math.sin(deg*Math.PI/180).toFloat()
        }
        fun stopped():Boolean = deg == 0f
    }
}