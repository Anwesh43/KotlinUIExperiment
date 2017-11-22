package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 22/11/17.
 */

class ArcLineMoverView(ctx:Context):View(ctx) {
    val renderer = ArcLineMoverRenderer(this)
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
    data class ArcLineMover(var w:Float,var h:Float) {
        var state = ArcLineMoverState()
        fun draw(canvas:Canvas,paint:Paint) {
            val deg = 90*state.scale
            paint.color = Color.parseColor("#E65100")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = Math.min(w,h)/40
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.save()
            canvas.rotate(deg)
            canvas.drawLine(0f,0f,w/3,0f,paint)
            canvas.restore()
            canvas.drawArc(RectF(-w/3,-w/3,w/3,w/3),0f,deg,false,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()

    }
    data class ArcLineMoverState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 10f
            if(deg > 180) {
                deg = 0f
            }
            scale = Math.sin(deg*Math.PI/180).toFloat()
        }
        fun stopped():Boolean = deg == 0f
    }
    data class ArcLineMoverAnimator(var arcLineMover:ArcLineMover,var view:ArcLineMoverView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            arcLineMover.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                arcLineMover.update()
                if(arcLineMover.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception){

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class ArcLineMoverRenderer(var view:ArcLineMoverView,var time:Int = 0) {
        var animator:ArcLineMoverAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ArcLineMoverAnimator(ArcLineMover(w,h),view)
                paint.strokeCap = Paint.Cap.ROUND
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
        fun create(activity:Activity):ArcLineMoverView {
            val view = ArcLineMoverView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
            return view
        }
    }
}