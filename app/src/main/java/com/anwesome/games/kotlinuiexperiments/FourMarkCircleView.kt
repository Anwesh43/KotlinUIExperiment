package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 08/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*

class FourMarkCircleView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = FourMarkCircleRenderer(this)
    var clickListener:FourMarkCircleClickListener?=null
    fun addClickListener(listener:()->Unit) {
        clickListener = FourMarkCircleClickListener(listener)
    }
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
    data class FourMarkCircle(var w:Float,var h:Float) {
        var state = FourMarkState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.WHITE
            canvas.drawCircle(0f,0f,w/3,paint)
            paint.color = Color.GRAY
            canvas.rotate(90f*state.scale)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.save()
                canvas.translate(w/4,0f)
                canvas.drawArc(RectF(-w/12,-w/12,w/12,w/12),-15f,30f,true,paint)
                canvas.restore()
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
    data class FourMarkState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale  = (prevScale+1)%2
                prevScale = scale
                if(dir == 1.0f) {
                    dir = -1.0f
                }
                else {
                    dir = 0.0f
                }
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1.0f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    class FourMarkCircleAnimator(var fourMarkCircle:FourMarkCircle,var view:FourMarkCircleView) {
        var animated = false
        fun update() {
            if(animated) {
                fourMarkCircle.update()
                if(fourMarkCircle.stopped()) {
                    view.clickListener?.onClickListener?.invoke()
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
            fourMarkCircle.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                fourMarkCircle.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class FourMarkCircleRenderer(var view:FourMarkCircleView,var time:Int = 0) {
        var animator:FourMarkCircleAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = FourMarkCircleAnimator(FourMarkCircle(w,h),view)
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
        fun create(activity:Activity):FourMarkCircleView {
            var view = FourMarkCircleView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
            return view
        }
    }
    data class FourMarkCircleClickListener(var onClickListener:()->Unit)
}