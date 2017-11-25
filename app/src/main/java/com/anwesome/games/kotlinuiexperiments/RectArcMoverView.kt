package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 25/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class RectArcMoverView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RectArcMoverRenderer(this)
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
    data class RectBarUpDown(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(0f,0f)
                canvas.scale(1f,1f-2*i)
                canvas.drawRect(RectF(-w/2,-h/2,w/2,-h/2+((h/2)*scale)),paint)
                canvas.restore()
            }
        }
    }
    data class CenterArc(var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*scale,true,paint)
            canvas.restore()
        }
    }
    data class RectArcMoverContainer(var w:Float,var h:Float,var arc:CenterArc = CenterArc(Math.min(w,h)/10),var rectBar:RectBarUpDown = RectBarUpDown(w,h)) {
        var state = RectArcMoverState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            rectBar.draw(canvas,paint,state.scales[0])
            arc.draw(canvas,paint,state.scales[1])
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
    data class RectArcMoverState(var dir:Int = 0,var prevDir:Int = 0,var j:Int = 0,var prevScale:Float = 0f) {
        var scales:Array<Float> = arrayOf(0f,0f)
        fun update() {
            scales[j] += dir*0.1f
            if(Math.abs(scales[j]-prevScale) > 1f) {
                scales[j] = prevScale+dir
                j+=dir
                if(j == scales.size || j == -1) {
                    prevDir*=-1
                    j+=prevDir
                }
            }
        }
        fun startUpdating() {
            dir = prevDir
            prevScale = scales[j]
        }
        fun stopped():Boolean = dir == 0
    }
    data class RectArcMoverAnimator(var container:RectArcMoverContainer,var view:RectArcMoverView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                container.update()
                if(container.stopped()) {
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
                container.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class RectArcMoverRenderer(var view:RectArcMoverView,var time:Int = 0) {
        var animator:RectArcMoverAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = RectArcMoverAnimator(RectArcMoverContainer(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
}