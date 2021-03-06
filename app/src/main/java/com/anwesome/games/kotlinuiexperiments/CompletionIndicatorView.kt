package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 21/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class CompletionIndicatorView(ctx:Context,var n:Int):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CompletionIndicatorRenderer(this)
    var completionListener:CompletionIndicatorListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    fun addCompletionIndicatorListener(cb:(Int)->Unit) {
        completionListener = CompletionIndicatorListener(cb)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class CompletionIndicator(var x:Float,var y:Float,var r:Float,var gap:Float,var deg:Float = 0f,var prevDeg:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.color = Color.GRAY
            canvas.drawCircle(x,y,r,paint)
            paint.color = Color.parseColor("#C51162")
            canvas.drawArc(RectF(x-r,y-r,x+r,y+r),0f,deg,false,paint)
        }
        fun update(scale:Float) {
            deg = prevDeg+gap*scale
        }
        fun setDeg() {
            deg = prevDeg+gap
            prevDeg = deg
        }
    }
    data class LineIndicator(var x:Float,var y:Float,var w:Float,var k:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#C51162")
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(x,y,x+k,y,paint)
        }
        fun update(scale:Float) {
            k = w*scale
        }
        fun setK() {
            k = w
        }
    }
    data class CompletionIndicatorState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                scale = 0f
                dir = 1f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    data class CompletionIndicatorContainer(var w:Float,var h:Float,var n:Int) {
        var completionIndicator = CompletionIndicator(w/2,h/5,h/12,360f/n)
        var lineIndicators:ConcurrentLinkedQueue<LineIndicator> = ConcurrentLinkedQueue()
        var j:Int = 0
        var state = CompletionIndicatorState()
        init {
            var y = h/5 + h/9
            val gapY = (h-y)/(n+1)
            y+= gapY
            for(i in 0..n-1){
                lineIndicators.add(LineIndicator(w/20,y,9*w/10))
                y+=gapY
            }
        }
        private fun draw(canvas:Canvas,paint:Paint) {
            completionIndicator.draw(canvas,paint)
            lineIndicators.forEach { lineIndicator ->
                lineIndicator.draw(canvas,paint)
            }
        }
        fun render(canvas:Canvas,paint:Paint,stopcb:()->Unit) {
            update(stopcb)
            draw(canvas,paint)
        }
        fun handleTap(startcb:()->Unit) {
            if(j < n) {
                state.startUpdating()
                startcb()
            }
        }
        private fun update(stopcb:()->Unit) {
            if(j < n && state.dir != 0f) {
                state.update()
                completionIndicator.update(state.scale)
                lineIndicators.getAt(j)?.update(state.scale)
                if(state.stopped()) {
                    completionIndicator.setDeg()
                    lineIndicators.getAt(j)?.setK()
                    stopcb()
                    j++
                }
            }
        }
    }
    class CompletionIndicatorAnimator(var container:CompletionIndicatorContainer,var view:CompletionIndicatorView) {
        var animated:Boolean = false
        fun render(canvas:Canvas,paint:Paint) {
            container.render(canvas,paint,{
                animated = false
                view.completionListener?.onComplete?.invoke(container.j)
            })
            if(animated) {
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                container.handleTap {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    class CompletionIndicatorRenderer(var view:CompletionIndicatorView,var time:Int = 0) {
        var animator:CompletionIndicatorAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = CompletionIndicatorAnimator(CompletionIndicatorContainer(w,h,view.n),view)
                paint.strokeWidth = w/35
            }
            animator?.render(canvas,paint)
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        fun create(activity:Activity,n:Int):CompletionIndicatorView {
            val view = CompletionIndicatorView(activity,n)
            activity.setContentView(view)
            return view
        }
    }
    data class CompletionIndicatorListener(var onComplete:(Int)->Unit)
}
fun ConcurrentLinkedQueue<CompletionIndicatorView.LineIndicator>.getAt(i:Int):CompletionIndicatorView.LineIndicator? {
    var index:Int = 0
    this.forEach {
        if(i == index) {
            return it
        }
        index++
    }
    return null
}