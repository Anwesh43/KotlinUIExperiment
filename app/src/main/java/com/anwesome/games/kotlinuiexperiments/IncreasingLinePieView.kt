package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 04/12/17.
 */
import android.graphics.*
import android.view.*
import android.content.*
import java.util.concurrent.ConcurrentLinkedQueue

class IncreasingLinePieView(ctx:Context):View(ctx) {
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
    data class IncreasingLine(var x:Float,var y:Float,var w:Float) {
        var state = IncreasingLineState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawLine(0f,0f,w*state.scale,0f,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class IncreasingLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
    data class IncreasingLineContainer(var w:Float,var h:Float,var n:Int) {
        val lines:ConcurrentLinkedQueue<IncreasingLine> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val gap = h/(n+1)
                val x = w/10
                var y = gap
                for(i in 0..n - 1) {
                    lines.add(IncreasingLine(x,y,gap*(i+1)))
                    y += gap
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            lines.forEach { line ->
                line.draw(canvas,paint)
            }
        }
        fun update(stopcb:(Float,Int)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}