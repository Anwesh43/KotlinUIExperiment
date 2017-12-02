package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 02/12/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class CrossPieView(ctx:Context):View(ctx) {
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
    data class CrossPie(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(w/2,h/10)
            paint.strokeWidth = Math.min(w,h)/50
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,h/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),0f,360f*scale,true,paint)
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w / 2, 3 * h / 5)
                canvas.rotate(-45f-90f*i)
                canvas.drawLine(0f, -w / 3, 0f, -w / 3 + 2 * w / 3 * scale)
                canvas.restore()
            }
        }
    }
    data class CrossPieContainer(var w:Float,var h:Float) {
    }
}