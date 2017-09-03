package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 04/09/17.
 */
class RectCompArcView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RCARenderer()
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class RectCompArc(var w:Float,var h:Float,var state:RCAState = RCAState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#3F51B5")
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w / 2, h / 2)
                canvas.scale(1f-2*i,1f)
                canvas.drawArc(RectF(-0.45f*w,-0.05f*w,-0.35f*w,0.05f*w),0f,360f*state.scale,true,paint)
                canvas.drawRect(RectF(-0.35f*w,-0.05f*w,-0.35f*w+(0.35f*w)*(1-this.state.scale),0.05f*w),paint)
                canvas.restore()
            }
        }
        fun update() {
            this.state.update()
        }
        fun startUpdating() {
            this.state.startUpdating()
        }
        fun stopped():Boolean = this.state.stopped()
    }
    data class RCAState(var scale:Float = 0f,var dir:Int = 0) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0
            }
            if(scale < 0) {
                dir = 0
                scale = 0f
            }
        }
        fun stopped():Boolean {
            return dir == 0
        }
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
    }
    class RCAAnimator(var rectCompArc:RectCompArc,var view:RectCompArcView,var animated:Boolean= false) {
        fun draw(canvas: Canvas,paint: Paint) {
            rectCompArc.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                rectCompArc.update()
                if(rectCompArc.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                rectCompArc.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class RCARenderer {
        var time = 0
        var animator:RCAAnimator?=null
        fun render(canvas:Canvas,paint:Paint,view:RectCompArcView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var rectCompArc = RectCompArc(w,h)
                animator = RCAAnimator(rectCompArc,view)
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
        fun create(activity:Activity) {
            var view = RectCompArcView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x/10))
        }
    }
}