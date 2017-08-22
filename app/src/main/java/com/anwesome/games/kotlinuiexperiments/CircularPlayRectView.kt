package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 22/08/17.
 */
class CircularPlayRectView(ctx:Context):View(ctx) {
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
    data class CircularPlayRect(var w:Float,var h:Float,var scale:Float = 0.0f) {
        var circularPlay:CircularPlay?=null
        init {
            circularPlay = CircularPlay(w/2,h/2,Math.min(w,h)/5)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            circularPlay?.draw(canvas,paint,scale)
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.scale(scale,1.0f)
            canvas.drawRect(RectF(-w/2,-h/2,w/2,h/2),paint)
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = circularPlay?.handleTap(x,y)?:false
    }
    data class CircularPlay(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            var path = Path()
            path.moveTo(-r/6,-r/6)
            path.lineTo(-r/6,r/6)
            path.lineTo(r/6,0.0f)
            paint.style = Paint.Style.STROKE
            paint.color = Color.WHITE
            canvas.drawPath(path,paint)
            canvas.save()
            canvas.scale(scale,scale)
            paint.style = Paint.Style.FILL
            canvas.drawPath(path, paint)
            canvas.restore()
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y <= this.y+r
    }
}