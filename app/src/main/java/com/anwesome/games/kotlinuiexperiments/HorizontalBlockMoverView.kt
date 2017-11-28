package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 28/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class HorizontalBlockMoverView(ctx:Context,var n:Int = 4):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = HorizontalBlockMoverRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class HorizontalBlockMover(var i:Int,var prevX:Float,var y:Float,var w:Float,var hor_w:Float,var x:Float = prevX - hor_w) {
        var state = HorizontalBlockMoverState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x+hor_w,y)
            canvas.drawRect(RectF(-w/2,-w/2,w/2,w/2),paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class HorizontalBlockMoverState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                prevScale = scale
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class HorizontalBlockMoverContainer(var w:Float,var h:Float,var n:Int,var dir:Int = 1,var j:Int = 0) {
        var blocks:ConcurrentLinkedQueue<HorizontalBlockMover> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val h_gap = h/(2*n)
                var x = w / 2 - h_gap
                var y = h/2 - (h_gap*n)/2 + h_gap/2
                for (i in 0..n) {
                    blocks.add(HorizontalBlockMover(i,x,y,w/10,w/2))
                    if(i % 2 == 1) {
                        y += h_gap
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            blocks.forEach { block ->
                block.draw(canvas,paint)
            }
        }
        fun update(stopcb:()->Unit) {
            blocks.getCurr(j)?.update()
            if(blocks.getCurr(j)?.stopped()?:false) {
                j+=dir
                if(j == blocks.size || j == -1) {
                    dir *= -1
                    j += dir

                }
                stopcb()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            blocks.getCurr(j)?.startUpdating()
            startcb()
        }
    }
    data class HorizontalBlockMoverAnimator(var container:HorizontalBlockMoverContainer,var view:HorizontalBlockMoverView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{
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
            container.startUpdating {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class HorizontalBlockMoverRenderer(var view:HorizontalBlockMoverView,var time:Int = 0) {
        var animator:HorizontalBlockMoverAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = HorizontalBlockMoverAnimator(HorizontalBlockMoverContainer(w,h,view.n),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun startUpdating() {
            animator?.handleTap()
        }
    }
    companion object {
        fun create(activity:MainActivity):HorizontalBlockMoverView {
            val view = HorizontalBlockMoverView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
fun ConcurrentLinkedQueue<HorizontalBlockMoverView.HorizontalBlockMover>.getCurr(i:Int):HorizontalBlockMoverView.HorizontalBlockMover? {
    var index = 0
    this.forEach { it ->
        if(index == i) {
            return it
        }
        index++
    }
    return null
}