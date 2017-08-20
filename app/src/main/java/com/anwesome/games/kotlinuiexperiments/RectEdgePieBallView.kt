package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 21/08/17.
 */
class RectEdgePieBallView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class PieBall(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class EdgeBall(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(scale,scale)
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
    }
    data class RectEdgePieBall(var w:Float,var h:Float,var scale:Float=0.0f) {
        var edgeBalls:ConcurrentLinkedQueue<EdgeBall> = ConcurrentLinkedQueue()
        var pieBall:PieBall?=null
        init {
            var size = 2*Math.min(w,h)/3
            var edgeSize = size/(9)
            var x = 3*edgeSize/2
            for(i in 0..3) {
                var edgeBall = EdgeBall(x,-h/3, edgeSize)
                edgeBalls.add(edgeBall)
                x += 2*edgeSize
            }
            pieBall = PieBall(w/2,h/2,size/5)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            pieBall?.draw(canvas,paint,scale)
            edgeBalls.forEach { edgeBall ->
                edgeBall.draw(canvas,paint,scale)
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = pieBall?.handleTap(x,y)?:false
    }
}