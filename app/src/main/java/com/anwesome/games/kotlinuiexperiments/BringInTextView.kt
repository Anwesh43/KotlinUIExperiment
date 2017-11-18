package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 16/11/17.
 */

class BringInTextView(ctx:Context,var text:String="Hello"):View(ctx) {
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
    data class BringInText(var w:Float,var h:Float,var text:String) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawRoundRect(RectF(-w/3,-h/15,w/3,h/15),h/10,h/10,paint)
            canvas.save()
            canvas.scale(1f,1f)
            paint.color = Color.parseColor("#00E5FF")
            canvas.drawRoundRect(RectF(-w/3,-h/15,w/3,h/15),h/10,h/10,paint)
            canvas.restore()
            paint.textSize = h/15
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f-2*i,1f)
                canvas.drawText(text,-w/2+(w/2),(1f-2*i)*(h/5),paint)
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