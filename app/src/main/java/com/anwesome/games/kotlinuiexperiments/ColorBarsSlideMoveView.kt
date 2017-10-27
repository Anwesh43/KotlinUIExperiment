package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 27/10/17.
 */
val slideColors:Array<String> = arrayOf("#009688","#FF6F00","#0277BD","#f44336","#283593")
class ColorBarSlideMoveView(ctx:Context):View(ctx) {
    var listener:ColorBarSlideFillListener?=null
    val renderer = ColorBarSlideMoveRenderer(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class ColorSlideMove(var i:Int,var x:Float,var y:Float,var w:Float,var h:Float) {
        var state = ColorSlideMoveState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor(slideColors[i])
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(0f,0f,w*state.scale,h),paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
    }
    data class ColorSlideMoveState(var scale:Float=0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
    class ColorBarsSlideContainer(var w:Float,var h:Float) {
        var j:Int = 0
        var dir:Int = 0
        var currDir:Int = 1
        var bars:ConcurrentLinkedQueue<ColorSlideMove> = ConcurrentLinkedQueue()
        init {
            if(slideColors.size > 0) {
                var size = (w / 2) / slideColors.size
                var x = w/4
                for (i in 0..slideColors.size - 1) {
                    bars.add(ColorSlideMove(i,x,h/2-w/4,size,w/2))
                    x += size
                }
            }
        }
        fun update(view: ColorBarSlideMoveView,stopcb:()->Unit) {
            val curr = bars.getAt(j)
            curr?.update()
            if(curr?.stopped()?:false) {
                j+=dir
                bars.getAt(j)?.startUpdating()
                when(dir) {
                    1 -> view.listener?.fillListener?.invoke(j)
                    -1 -> view.listener?.emptyListener?.invoke(j)
                }
                if(j == bars.size || j == -1) {
                    currDir *=-1
                    j+=currDir
                    dir = 0
                    stopcb()
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            bars.forEach { bar ->
                bar.draw(canvas, paint)
            }
            canvas.restore()
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0) {
                bars.getAt(j)?.startUpdating()
                dir = currDir
                startcb()
            }
        }
    }
    class ColorBarSlideMoveAnimator(var container:ColorBarsSlideContainer,var view:ColorBarSlideMoveView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                container.update(view,{
                    animated = false
                })
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
            container.startUpdating {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class ColorBarSlideMoveRenderer(var view:ColorBarSlideMoveView) {
        var time = 0
        var animator:ColorBarSlideMoveAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                var container = ColorBarsSlideContainer(w,h)
                animator = ColorBarSlideMoveAnimator(container,view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        var view:ColorBarSlideMoveView?=null
        fun create(activity:Activity) {
            view = ColorBarSlideMoveView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
        fun addFillListener(fillListener: (Int) -> Unit, emptyListener:(Int)->Unit) {
            view?.listener = ColorBarSlideFillListener(fillListener,emptyListener)
        }
    }
    data class ColorBarSlideFillListener(var fillListener:(Int)->Unit,var emptyListener:(Int)->Unit)
}
fun ConcurrentLinkedQueue<ColorBarSlideMoveView.ColorSlideMove>.getAt(i:Int):ColorBarSlideMoveView.ColorSlideMove? {
    var index = 0
    var curr:ColorBarSlideMoveView.ColorSlideMove ?=null
    this.forEach {
        if(i == index) {
            curr = it
            return it
        }
        index++
    }
    return curr
}
