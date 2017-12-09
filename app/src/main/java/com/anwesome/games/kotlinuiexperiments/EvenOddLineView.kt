package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/12/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class EvenOddLineView(ctx:Context,var n:Int = 10):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = EvenOddLineRenderer(this)
    var onOddEvenListener:OnEvenOddListener?=null
    fun addListener(onOddListener: () -> Unit,onEvenListener: () -> Unit) {
        onOddEvenListener = OnEvenOddListener(onOddListener, onEvenListener)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.startUpdating()
            }
        }
        return true
    }
    data class EvenOddLine(var i:Int,var x:Float,var py:Float,var h:Float,var y:Float=py) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            val j = i%2
            val new_scale = (j+(1-2*j)*scale)
            y = - h*new_scale
            canvas.save()
            canvas.translate(x,py)
            canvas.drawLine(0f,0f,0f,y,paint)
            canvas.drawCircle(0f,y,(h/20)*new_scale,paint)
            canvas.restore()
        }
    }
    data class EvenOddLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1){
                scale = prevScale + dir
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
    data class EvenOddLineContainer(var w:Float,var h:Float,var n:Int) {
        var state = EvenOddLineState()
        var lines:ConcurrentLinkedQueue<EvenOddLine> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val gap = w/(n+1)
                var x = gap
                val y = 9*h/10
                val h = 2*h/5
                for(i in 0..n-1) {
                    lines.add(EvenOddLine(i,x,y,h))
                    x += gap
                }
            }
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            lines.forEach { line ->
                line.draw(canvas,paint,state.scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class EvenOddLineAnimator(var container:EvenOddLineContainer,var view:EvenOddLineView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update { scale ->
                    animated = false
                    when(scale) {
                        0f -> view.onOddEvenListener?.onEvenListener?.invoke()
                        1f -> view.onOddEvenListener?.onOddListener?.invoke()
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun startUpdating() {
            if(!animated) {
                container.startUpdating{
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class EvenOddLineRenderer(var view:EvenOddLineView,var time:Int = 0) {
        var animator:EvenOddLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = EvenOddLineAnimator(EvenOddLineContainer(w,h,view.n),view)
                paint.strokeWidth = Math.min(w,h)/50
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = Color.parseColor("#01579B")
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun startUpdating() {
            animator?.startUpdating()
        }
    }
    companion object {
        fun create(activity:Activity):EvenOddLineView {
            val view = EvenOddLineView(activity)
            activity.setContentView(view)
            return view
        }
    }
    data class OnEvenOddListener(var onOddListener:()->Unit,var onEvenListener:()->Unit)
}