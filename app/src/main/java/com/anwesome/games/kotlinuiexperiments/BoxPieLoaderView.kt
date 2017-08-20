package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 20/08/17.
 */
class BoxPieLoaderView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class BoxPieLoader(var h:Float,var w:Float,var scale:Float=0.0f) {
        var r = Math.min(w,h)/15
        fun draw(canvas:Canvas,paint:Paint) { 
            paint.strokeWidth = r/3
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            for(i in 0..4) {
                canvas.save()
                canvas.rotate(90.0f*i)
                canvas.drawLine(-(0.4f*w)*scale,-0.4f*h,(0.4f*w)*scale,0.4f*h,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.w/2-r && x<=this.w/2+r && y>=this.h/2-r && y<=this.h/2 +r
    }
}