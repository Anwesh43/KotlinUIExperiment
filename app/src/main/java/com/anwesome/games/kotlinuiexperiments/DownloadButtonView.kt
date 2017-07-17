package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 18/07/17.
 */
class DownloadButtonView(ctx:Context):View(ctx) {
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
    data class DownloadButtonShape(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.parseColor("#03A9F4")
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360*scale,true,paint)
            paint.color = Color.WHITE
            canvas.drawLine(0.0f,-r/2,0.0f,r/2,paint)
            for(i in 0..2) {
                canvas.save()
                canvas.translate(0.0f, r / 2)
                canvas.rotate(45.0f*(i*2-1))
                canvas.drawLine(0.0f,0.0f,0.0f,-r/6,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean =  x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class DBVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:DownloadButtonView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class DBVDrawingController(var shape:DownloadButtonShape ,var view:DownloadButtonView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            shape.draw(canvas,paint,1.0f)
            if(animated) {
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && shape.handleTap(x,y)) {
                animated = true
                view.postInvalidate()
            }
        }
    }
}