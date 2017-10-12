package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 12/10/17.
 */
class DoubleTouchingTriangleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DTTRenderer(this)
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
    data class DoubleTouchingTriangle(var x:Float,var y:Float,var size:Float,var state:DTTState = DTTState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f-2*i,1f)
                canvas.save()
                canvas.drawRotatedHorizontalTriangle(size/2,0f,180f*state.scale,size,paint)
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
    data class DTTState(var scale:Float = 0f,var dir:Float = 0f) {
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
            this.dir = 1f-2*this.scale
        }
        fun stopped() = dir == 0f
    }
    class DTTAnimator(var dtt:DoubleTouchingTriangle,var view:DoubleTouchingTriangleView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                dtt.update()
                if(dtt.stopped()) {
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
        fun handleTap() {
            if(!animated) {
                dtt.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            dtt.draw(canvas,paint)
        }
    }
    class DTTRenderer(var view:DoubleTouchingTriangleView,var time:Int = 0) {
        var animator:DTTAnimator ?= null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = DTTAnimator(DoubleTouchingTriangle(w/2,h/2,Math.min(w,h)/3),view)
                paint.color = Color.parseColor("#00838F")
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
            val view = DoubleTouchingTriangleView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}
fun Path.addHorizontalTriangle(size:Float) {
    this.moveTo(-size/2,0f)
    this.lineTo(size/2,-size/2)
    this.lineTo(size/2,size/2)
}
fun Canvas.drawRotatedHorizontalTriangle(x:Float,y:Float,deg:Float,size:Float,paint:Paint) {
    this.save()
    this.translate(x,y)
    this.rotate(deg)
    val path = Path()
    path.addHorizontalTriangle(size)
    this.drawPath(path,paint)
    this.restore()
}