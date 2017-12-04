package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 04/12/17.
 */
import android.app.Activity
import android.graphics.*
import android.view.*
import android.content.*
import java.util.concurrent.ConcurrentLinkedQueue

class IncreasingLinePieView(ctx:Context,var n:Int = 5):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = IncreasingLineRenderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
                val h_gap = (4*h/5)/(n+1)
                val w_gap = (4*w/5)/(n+1)
                val x = w/10
                var y = h/5+h_gap
                for(i in 0..n - 1) {
                    lines.add(IncreasingLine(x,y,w_gap*(i+1)))
                    y += h_gap
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
                val gap = 360f/n
                canvas.save()
                canvas.translate(w/2,h/10)
                var scale = lines.at(j)?.state?.scale?:0f
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(0f,0f,h/12,paint)
                paint.style = Paint.Style.FILL
                canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),0f,gap*j+gap*scale,true,paint)
                canvas.restore()
            }
        }
        fun update(stopcb:(Float,Int)->Unit) {
            state.execFunc { j ->
                lines.at(j)?.update { scale ->
                    stopcb(scale,j)
                    state.incrementCounter()
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
    data class IncreasingLineAnimator(var container:IncreasingLineContainer,var view:IncreasingLinePieView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{scale,j ->
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                container.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class IncreasingLineRenderer(var view:IncreasingLinePieView,var time:Int = 0) {
        var animator:IncreasingLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = IncreasingLineAnimator(IncreasingLineContainer(w,h,view.n),view)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        fun create(activity:Activity):IncreasingLinePieView {
            val view = IncreasingLinePieView(activity)
            activity.setContentView(view)
            return view
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