package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 23/08/17.
 */
class PieCornerBallMoverView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class PieCornerBallMover(var w:Float,var h:Float,var r:Float=Math.min(w,h)/10,var scale:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#26A69A")
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.strokeWidth = r/8
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(i*90.0f+45.0f)
                canvas.drawCircle(0.0f,-h/3*scale,r,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.w/2-r && x<=this.w/2+r && y>=this.h/2 -r && y<=this.h/2+r
        fun update(scale:Float) {
            this.scale = scale
        }
    }
}