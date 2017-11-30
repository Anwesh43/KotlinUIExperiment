package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class AlternateLinePieView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = AlternateLineRenderer(this)
    var lineSelectionListener:AlternateLineSelectionListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    fun addLineSelectionListener(selectionListener: (Int) -> Unit, unselectionListener: (Int) -> Unit) {
        lineSelectionListener = AlternateLineSelectionListener(selectionListener, unselectionListener)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.startUpdating()
            }
        }
        return true
    }
    data class AlternateLine(var i:Int,var x:Float,var y:Float,var h:Float) {
        var state = AlternateLineState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#b71c1c")
            canvas.save()
            canvas.translate(x,y)
            when(i%2) {
                0 -> canvas.drawLine(0f,0f,0f,h*state.scale,paint)
                1 -> canvas.drawLine(0f,h,0f,h*(1-state.scale),paint)
            }
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class AlternateLineContainer(var w:Float,var h:Float,var n:Int) {
        var state = AlternateLineContainerState(n)
        var lines:ConcurrentLinkedQueue<AlternateLine> = ConcurrentLinkedQueue()
        init {
            val gap = w/(n+1)
            var x = gap
            for(i in 0..n-1) {
                lines.add(AlternateLine(i,x,0f,4*h/5-h/20))
                x += gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            if(n > 0) {
                val j = state.j
                val scale = (lines.getAt(j)?.state?.scale?:0f)
                val degGap = 360f/n
                paint.strokeWidth = (lines.getAt(0)?.x?:10f)/10
                paint.color = Color.parseColor("#D84315")
                canvas.save()
                canvas.translate(w/2,h/10)
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(0f,0f,h/12,paint)
                paint.style = Paint.Style.FILL
                canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),0f,j*degGap+degGap*scale,true,paint)
                canvas.restore()
                canvas.save()
                canvas.translate(0f,h/5)
                lines.forEach { line ->
                    line.draw(canvas,paint)
                }
                canvas.restore()
            }
        }
        fun update(stopcb:(Int,Float)->Unit) {
            lines.getAt(state.j)?.update({scale ->
                stopcb(state.j,scale)
                state.updateJOnStop()
            })
        }
        fun startUpdating(startcb:()->Unit) {
            lines.getAt(state.j)?.startUpdating(startcb)
        }
    }
    data class AlternateLineContainerState(var n:Int,var j:Int = 0,var dir:Int = 0,var prevDir:Int = 1) {
        fun updateJOnStop() {
            dir = 0
            j += prevDir
            if(j == n || j == -1) {
                prevDir*=-1
                j += prevDir
            }
        }
    }
    data class AlternateLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f - 2*scale
            startcb()
        }
    }
    data class AlternateLineAnimator(var container:AlternateLineContainer,var view:AlternateLinePieView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{ i,scale ->
                    animated = false
                    when(scale) {
                        1f -> view.lineSelectionListener?.selectionListener?.invoke(i)
                        0f -> view.lineSelectionListener?.unselectionListener?.invoke(i)
                    }
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
        fun startUpdating() {
            if(!animated) {
                container.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class AlternateLineRenderer(var view:AlternateLinePieView,var time:Int = 0) {
        var animator:AlternateLineAnimator? = null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = AlternateLineAnimator(AlternateLineContainer(w,h,view.n),view)
                paint.strokeCap = Paint.Cap.ROUND
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
        fun create(activity:Activity):AlternateLinePieView {
            val view = AlternateLinePieView(activity)
            activity.setContentView(view)
            return view
        }
    }
    data class AlternateLineSelectionListener(var selectionListener:(Int)->Unit,var unselectionListener:(Int)->Unit)
}
fun ConcurrentLinkedQueue<AlternateLinePieView.AlternateLine>.getAt(i:Int):AlternateLinePieView.AlternateLine? {
    var index = 0
    this.forEach {
        if(i == index) {
            return it
        }
        index++
    }
    return null
}