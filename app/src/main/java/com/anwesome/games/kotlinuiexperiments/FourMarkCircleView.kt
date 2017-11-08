package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 08/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class FourMarkCircleView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class FourMarkCircle(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.drawCircle(0f,0f,w/3,paint)
            canvas.rotate(90f)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.save()
                canvas.translate(0f,-w/4)
                for(j in 0..1) {
                    canvas.save()
                    canvas.rotate(15f*(2*i-1))
                    canvas.drawLine(0f,-w/4,0f,-w/3,paint)
                    canvas.restore()
                }
                canvas.restore()
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}