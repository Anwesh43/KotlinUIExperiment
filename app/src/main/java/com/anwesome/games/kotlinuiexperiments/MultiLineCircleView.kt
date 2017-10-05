package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 06/10/17.
 */
class MultiLineCircleView(ctx:Context):View(ctx) {
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
    data class LineCircle(var x:Float,var y:Float,var hSize:Float,var rSize:Float,var state:LineCircleState = LineCircleState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,rSize/2,paint)
            canvas.drawArc(RectF(-rSize/2,-rSize/2,rSize/2,rSize/2),0f,360f*state.scale,true,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f,1-2*state.scale)
                canvas.drawLine(0f,0f,0f,(hSize/2-rSize/2)*state.scale,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-rSize/2 && x<=this.x+rSize/2 && y>=this.y-rSize/2 && y<=this.y+rSize/2
    }
    data class LineCircleState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class MultiLineCircleContainer(var w:Float,var h:Float) {
        var lineCircles:ConcurrentLinkedQueue<LineCircle> = ConcurrentLinkedQueue()
        var tappedCircles:ConcurrentLinkedQueue<LineCircle> = ConcurrentLinkedQueue()
        init {
            val n = 4
            val gap = w/(2*n+1)
            var x = 3*gap/2
            for(i in 0..n-1) {
                lineCircles.add(LineCircle(x,h/2,2*h/5,h/5))
                x+=2*gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            lineCircles.forEach { lineCircle ->
                lineCircle.draw(canvas,paint)
            }
        }
        fun update(stopCb:()->Unit) {
            tappedCircles.forEach { tappedCircle ->
                tappedCircle.update()
                if(tappedCircle.stopped()) {
                    tappedCircles.remove(tappedCircle)
                    if(tappedCircles.size == 0) {
                        stopCb()
                    }
                }
            }
        }
        fun handleTap(x:Float,y:Float,startCb:()->Unit) {
            lineCircles.forEach { lineCircle ->
                if(lineCircle.stopped()) {
                    lineCircle.startUpdating()
                    tappedCircles.add(lineCircle)
                    startCb()
                }
            }
        }
    }
}