package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 25/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class RectArcMoverView(ctx:Context):View(ctx) {
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
    data class RectBarUpDown(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(0f,0f)
                canvas.scale(1f,1f-2*i)
                canvas.drawRect(RectF(-w/2,-h/2,w/2,-h/2+((h/2)*scale)),paint)
                canvas.restore()
            }
        }
    }
    data class CenterArc(var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*scale,true,paint)
            canvas.restore()
        }
    }
}