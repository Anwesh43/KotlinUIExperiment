package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 03/12/17.
 */
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class ConcentricCircleIndicatorView(ctx:Context):View(ctx) {
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
    data class ConcentricCircle(var r:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/12
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f,false,paint)
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class ConcentricCircleContainer(var w:Float,var h:Float) {
        var circles:ConcurrentLinkedQueue<ConcentricCircle> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.parseColor("#ff9800")
            circles.forEach { circle ->
                circle.draw(canvas,paint)
            }
            canvas.restore()
        }
        fun update(stopcb:(Float,Int)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}