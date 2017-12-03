package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 03/12/17.
 */
import android.app.Activity
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class ConcentricCircleIndicatorView(ctx:Context,var n:Int=5):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ConcentricCircleIndicatorRenderer(this)
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
    data class ConcentricCircle(var r:Float) {
        var state = ConcentricCircleState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/12
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,false,paint)
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class ConcentricCircleContainer(var w:Float,var h:Float,var n:Int) {
        var state = ConcentricCircleContainerState(n)
        var circles:ConcurrentLinkedQueue<ConcentricCircle> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.parseColor("#ff9800")
            circles.forEach { circle ->
                circle.draw(canvas,paint)
            }
            canvas.restore()
            state.executeFn { j ->
                if(n > 0) {
                    paint.color = Color.parseColor("#4caf50")
                    paint.strokeWidth = h/40
                    paint.strokeCap = Paint.Cap.ROUND
                    val gap = (0.8f*w)/n
                    val scale = circles.at(j)?.state?.scale ?: 0f
                    canvas.drawLine(w / 10,h/10,w/10+gap*j+gap*scale,h/10,paint)
                }
            }
        }
        fun update(stopcb:(Float,Int)->Unit) {
            state.executeFn {  j ->
                circles.at(j)?.update{ scale ->
                    state.incrementCounter()
                    stopcb(scale,j)
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.executeFn { j->
                circles.at(j)?.startUpdating(startcb)
            }
        }
    }
    data class ConcentricCircleState(var scale:Float = 0f,var dir:Float=0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
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
    data class ConcentricCircleContainerState(var n:Int,var j:Int = 0,var dir:Int = 0) {
        fun incrementCounter() {
            j+=dir
            if(j == n || j == -1) {
                dir*=-1
                j+=dir
            }
        }
        fun executeFn(cb:(Int)->Unit) {
            cb(j)
        }
    }
    data class ConcentricCircleIndicatorAnimator(var view:ConcentricCircleIndicatorView) {
        var animated = true
        fun animate(cb:(()->Unit)->Unit) {
            if(animated) {
                cb{
                    animated = false
                }
                try {
                    Thread.sleep(50)
                }
                catch(ex:Exception) {

                }

            }
        }
        fun startAnimation(cb:(()->Unit)->Unit) {
            if(!animated) {
                cb {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class ConcentricCircleIndicatorRenderer(var view:ConcentricCircleIndicatorView,var time:Int = 0) {
        var animator = ConcentricCircleIndicatorAnimator(view)
        var container:ConcentricCircleContainer?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = ConcentricCircleContainer(w,h,view.n)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            container?.draw(canvas,paint)
            animator?.animate { stopcb ->
                container?.update{ scale,j ->
                    stopcb()
                }
            }
            time++
        }
        fun handleTap() {
            animator?.startAnimation { startcb ->
                container?.startUpdating(startcb)
            }
        }
    }
    companion object {
        fun create(activity:Activity):ConcentricCircleIndicatorView {
            val view = ConcentricCircleIndicatorView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
fun ConcurrentLinkedQueue<ConcentricCircleIndicatorView.ConcentricCircle>.at(index:Int):ConcentricCircleIndicatorView.ConcentricCircle? {
    var i = 0
    this.forEach {
        if(i == index) {
            return it
        }
        i++
    }
    return null
}