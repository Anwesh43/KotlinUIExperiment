package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 23/10/17.
 */
import android.app.ActionBar
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class RotateLineButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RLBRenderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(x,y)
            }
        }
        return true
    }
    data class RotateLineButton(var i:Int,var x:Float,var y:Float,var r:Float,var size:Float) {
        var state = RotateLineButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            canvas.scale(state.scale,state.scale)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(90f*(1-2*i)*state.scale)
            canvas.drawLine(0f,-r,0f,-r-size,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
        fun stopped():Boolean = state.stopped()
    }
    data class RotateLineButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale+=dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
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
    data class RotateLineButtonContainer(var w:Float,var h:Float) {
        var  rotatingLines:ConcurrentLinkedQueue<RotateLineButton> = ConcurrentLinkedQueue()
        var updatingLines:ConcurrentLinkedQueue<RotateLineButton> = ConcurrentLinkedQueue()
        init {
            for(i in 0..1) {
                rotatingLines.add(RotateLineButton(i,w/2-w/4+w/2*i,w/2,w/20,w/4))
            }
        }
        fun update(stopcb:()->Unit) {
            updatingLines.forEach { rlb ->
                rlb.update()
                if(rlb.stopped()) {
                    updatingLines.remove(rlb)
                    if(updatingLines.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            rotatingLines.forEach { rotatingLine ->
                rotatingLine.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            rotatingLines.forEach { rlb ->
                if(rlb.handleTap(x,y)) {
                    updatingLines.add(rlb)
                    if(updatingLines.size == 1) {
                        startcb()
                    }
                    return
                }
            }
        }
    }
    class RLBAnimator(var container:RotateLineButtonContainer,var view:RotateLineButtonView) {
        var animated = true
        fun update() {
            if(animated) {
                container.update({
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
        fun handleTap(x:Float,y:Float) {
            container.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
    }
    class RLBRenderer(var view:RotateLineButtonView,var time:Int = 0) {
        var animator:RLBAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = RLBAnimator(RotateLineButtonContainer(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object {
        var view:RotateLineButtonView?=null
        fun create(activity:Activity) {
            view = RotateLineButtonView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}