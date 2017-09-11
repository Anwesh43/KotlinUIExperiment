package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 11/09/17.
 */
class LineArcListView(ctx:Context):View(ctx) {
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
    data class LineArc(var x:Float,var y:Float,var r:Float,var h:Float,var state:LineArcState = LineArcState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,true,paint)
            canvas.drawLine(0f,-2*r,0f,-2*r-h*state.scale,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float) = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class LineArcState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale  = Math.abs(Math.cos(deg*Math.PI/180)).toFloat()
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    data class LineArcContainer(var w:Float, var h:Float, var n:Int, var lineArcs:ConcurrentLinkedQueue<LineArc> = ConcurrentLinkedQueue(), var tappedLineArcs:ConcurrentLinkedQueue<LineArc> = ConcurrentLinkedQueue()) {
        init {
            var size = w/(2*n+1)
            var x = 3*size/2
            for(i in 1..n) {
                var lineArc = LineArc(x,h-size,size/2,h-2*size)
                lineArcs.add(lineArc)
                x += 2*size
            }
        }
        fun update(stopcb:()->Unit) {
            tappedLineArcs.forEach { tappedLineArc ->
                tappedLineArc.update()
                if(tappedLineArc.stopped()) {
                    tappedLineArcs.remove(tappedLineArc)
                    if(tappedLineArcs.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun handleTap(x:Float,y:Float,startCb:()->Unit) {
            lineArcs.forEach { lineArc ->
                if(lineArc.handleTap(x,y)) {
                    tappedLineArcs.add(lineArc)
                    if(tappedLineArcs.size == 1) {
                        startCb()
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            lineArcs.forEach { lineArc ->
                lineArc.draw(canvas,paint)
            }
        }
    }
    class LineArcViewAnimator(var lineArcContainer:LineArcContainer,var view:LineArcListView,var isAnimated:Boolean = false) {
        fun draw(canvas:Canvas,paint:Paint) {
            lineArcContainer.draw(canvas,paint)
        }
        fun update() {
            if(isAnimated) {
                lineArcContainer.update { isAnimated = false }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch (ex: Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            lineArcContainer.handleTap(x,y,{
                isAnimated = true
                view.postInvalidate()
            })
        }
    }
}