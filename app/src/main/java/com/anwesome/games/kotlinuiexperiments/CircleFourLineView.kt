package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 06/12/17.
 */
import android.graphics.*
import android.content.*
import android.view.*

class CircleFourLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class CircleFourLine(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.strokeWidth = r/12
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(r-(0.8f*r*scale),0f,r,0f,paint)
                canvas.restore()
            }
            canvas.drawCircle(0f,0f,r/5,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r/5,-r/5,r/5,r/5),0f,360f,true,paint)
            canvas.restore()
        }
    }
}