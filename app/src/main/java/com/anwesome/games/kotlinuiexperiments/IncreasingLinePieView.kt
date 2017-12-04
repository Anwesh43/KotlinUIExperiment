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
        var state = IncreasingLineContainerState(n)
        val lines:ConcurrentLinkedQueue<IncreasingLine> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val gap = (4*h/5)/(n+1)
                val x = w/10
                var y = h/5+gap
                for(i in 0..n - 1) {
                    lines.add(IncreasingLine(x,y,gap*(i+1)))
                    y += gap
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#0D47A1")
            paint.strokeWidth = Math.min(w,h)/60
            paint.strokeCap = Paint.Cap.ROUND
            lines.forEach { line ->
                line.draw(canvas,paint)
            }
            state.execFunc { j ->
                canvas.save()
                canvas.translate(w/2,h/10)
                var scale = lines.at(j)?.state?.scale?:0f
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(0f,0f,h/12,paint)
                paint.style = Paint.Style.FILL
                canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),0f,360f*scale,true,paint)
                canvas.restore()
            }
        }
        fun update(stopcb:(Float,Int)->Unit) {
            state.execFunc { j ->
                lines.at(j)?.update { scale ->
                    stopcb(scale,j)
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.execFunc { j->
                lines.at(j)?.startUpdating(startcb)
            }
        }
    }
    data class IncreasingLineContainerState(var n:Int,var j:Int = 0,var dir:Int = 1) {
        fun incrementCounter(){
            j+=dir
            if(j == n || j == -1) {
                dir*=-1
                j+=dir
            }
        }
        fun execFunc(cb:(Int)->Unit) {
            if(j < n) {
                cb(j)
            }
        }
    }
}
fun ConcurrentLinkedQueue<IncreasingLinePieView.IncreasingLine>.at(index:Int):IncreasingLinePieView.IncreasingLine? {
    var i = 0
    this.forEach {
        if(i == index) {
            return it
        }
        i++
    }
    return null
}