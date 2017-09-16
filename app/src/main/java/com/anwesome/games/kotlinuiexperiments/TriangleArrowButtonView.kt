package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 16/09/17.
 */
class  TriangleArrowButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = TriangleButtonRenderer()
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
    data class TriangleArrowButton(var x:Float,var y:Float,var l:Float,var r:Float,var state:TriangleArrowButtonState = TriangleArrowButtonState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.argb((255*state.scale).toInt(),255,255,255)
            canvas.save()
            canvas.translate(x,y)
            val n = 8
            val deg = 360f/n
            for(i in 0..n-1) {
                canvas.save()
                canvas.rotate(deg*i*state.scale)
                canvas.translate(0f,l*state.scale)
                var path = Path()
                path.moveTo(-r/2,r/2)
                path.lineTo(r/2,r/2)
                path.lineTo(0f,-r/2)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
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
    data class TriangleArrowButtonState(var scale:Float = 0f,var dir:Float = 0f) {
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
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class TriangleArrowButtonAnimator(var triangleButton:TriangleArrowButton,var view:TriangleArrowButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                triangleButton.update()
                if(triangleButton.stopped()) {
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
        fun draw(canvas: Canvas,paint: Paint) {
            triangleButton.draw(canvas,paint)
        }
        fun startUpdating() {
            if(!animated) {
                triangleButton.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class TriangleButtonRenderer {
        var time = 0
        var animator:TriangleArrowButtonAnimator?=null
        fun render(canvas:Canvas,paint:Paint,view:TriangleArrowButtonView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = TriangleArrowButtonAnimator(TriangleArrowButton(w/2,h/2,Math.min(w,h)/3,Math.min(w,h)/14),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.startUpdating()
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = TriangleArrowButtonView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}