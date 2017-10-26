package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.graphics.*
import android.view.*
import android.content.*
/**
 * Created by anweshmishra on 26/10/17.
 */
class RectEdgeRotatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RERRenderer(this)
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
    data class RectEdgeRotator(var x:Float,var y:Float,var size:Float){
        var state = RectEdgeRotatorState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            canvas.rotate(-30f*state.scales[2])
            canvas.drawLine(0f,0f,size*state.scales[0],0f,paint)
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(-30f*i)
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(size,0f,(size/12),paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(size,0f,(size/12)*state.scales[1],paint)
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
    data class RectEdgeRotatorState(var i:Int = 0,var dir:Int = 0,var currDir:Int = 1) {
        var scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update() {
            scales[i]+=dir*0.1f
            if(scales[i]>1 && dir == 1) {
                scales[i] = 1f
                incrementDir()
            }
            if(scales[i] < 0 && dir == -1) {
                scales[i] = 0f
                incrementDir()
            }
        }
        fun incrementDir() {
            i += currDir
            if(i == scales.size || i == -1) {
                currDir *= -1
                i+=currDir
                dir = 0
            }
        }
        fun startUpdating() {
            dir = currDir
        }
        fun stopped():Boolean = dir == 0
    }
    class RERAnimator(var rotator:RectEdgeRotator,var view:RectEdgeRotatorView) {
        var animated = false
        fun update() {
            if(animated) {
                rotator.update()
                if(rotator.stopped()) {
                    animated = false
                    view.invalidate()
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
                rotator.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            rotator.draw(canvas,paint)
        }
    }
    class RERRenderer(var view:RectEdgeRotatorView,var time:Int = 0) {
        var animator:RERAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = RERAnimator(RectEdgeRotator(w/5,0.5f*h,0.7f*w),view)
                paint.color = Color.parseColor("#FF9800")
                paint.strokeWidth = Math.min(w,h)/50
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
        var view:RectEdgeRotatorView?=null
        fun create(activity:Activity) {
            view = RectEdgeRotatorView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}