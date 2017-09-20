package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 21/09/17.
 */
class TwoColorRectView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = TwoColorRectRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class TwoColorRect(var x:Float,var y:Float,var size:Float,var state:TwoColorState = TwoColorState()) {
        fun draw(canvas:Canvas,paint:Paint) {

            canvas.save()
            canvas.translate(x,y)
            clipCirclePathAndDrawRect(0f,360f*state.scale,size,Color.parseColor("#f44336"),canvas,paint)
            clipCirclePathAndDrawRect(360f*state.scale,360f*(1-state.scale),size,Color.parseColor("#2196F3"),canvas,paint)
            canvas.restore()
        }
        private fun clipCirclePathAndDrawRect(degStart:Float,degSweep:Float,size:Float,color:Int,canvas: Canvas,paint:Paint) {
            val a = (size/Math.sqrt(2.0)).toFloat()
            canvas.save()
            val path = Path()
            path.moveTo(0f,0f)
            for(deg in degStart.toInt()..(degStart+degSweep).toInt()) {
                val x = (size/2)*Math.cos(deg*Math.PI/180).toFloat()
                val y = (size/2)*Math.sin(deg*Math.PI/180).toFloat()
                path.lineTo(x,y)
            }
            canvas.clipPath(path)
            paint.color = color
            canvas.drawRect(RectF(-a/2,-a/2,a/2,a/2),paint)
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
    data class TwoColorState(var scale:Float=0f,var dir:Float = 0f,var animated:Boolean = false) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1f-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class TwoColorRectAnimator(var rect:TwoColorRect,var view:TwoColorRectView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                rect.update()
                if(rect.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            rect.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                rect.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class TwoColorRectRenderer {
        var time = 0
        var animator:TwoColorRectAnimator?=null
        fun render(canvas: Canvas,paint:Paint,view: TwoColorRectView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                animator = TwoColorRectAnimator(TwoColorRect(w/2,h/2,Math.min(w,h)*0.8f),view)
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
            var view = TwoColorRectView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}