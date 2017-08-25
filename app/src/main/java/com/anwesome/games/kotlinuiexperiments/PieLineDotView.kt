package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 26/08/17.
 */
class PieLineDotView(ctx:Context,var n:Int=4):View(ctx) {
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
    data class LineDot(var radius:Float,var w:Float,var y:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(y,radius)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,radius,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawCircle(0.0f,0.0f,radius,paint)
            canvas.restore()
            canvas.drawLine(2.5f*radius,0.0f,2.5f*radius+(w-2.5f*radius)*scale,0.0f,paint)
            canvas.restore()
        }
    }
    data class PieLineDot(var w:Float,var h:Float,var n:Int,var x:Float = w/2,var y:Float = 9*h/10,var r:Float = h/10,var scale:Float = 0.0f) {
        var lineDots:ConcurrentLinkedQueue<LineDot> = ConcurrentLinkedQueue()
        init {
            var gap = (0.8f*h)/n
            var y = 3*gap/2
            for(i in 1..n) {
                var lineDot = LineDot(gap/2,w,y)
                lineDots.add(lineDot)
                y += 2*gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            lineDots.forEach { lineDot ->
                 lineDot.draw(canvas,paint,scale)
            }
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.translate(x,y)
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
}