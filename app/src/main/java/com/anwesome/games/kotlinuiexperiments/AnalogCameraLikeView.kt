package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 07/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
class AnalogCameraLikeView(ctx:Context):View(ctx) {
    val renderer = AnalogCameraRenderer(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class AnalogCameraShape(var w:Float,var h:Float) {
        var state = AnalogCameraState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawRoundRect(RectF(-w/3,-w/4,w/3,w/4),w/10,w/10,paint)
            paint.color = Color.parseColor("#212121")
            canvas.drawCircle(0f,0f,w/5,paint)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawCircle(0f,0f,w/10+w/10*state.scale,paint)
            val h = -(w/20*state.scale)
            canvas.drawRoundRect(RectF(-w/20,-w/4-w/20-h,w/20,-w/4-h),w/30,w/30,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-w/5 && x<=w/2+w/5 && y>=h/2-w/5 && y<=h/2+w/5
    }
    data class AnalogCameraState(var scale:Float = 0f,var deg:Float = 0f){
        fun update() {
            scale = Math.sin(deg*Math.PI/180).toFloat()
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
                scale = 0f
            }
        }
        fun stopped():Boolean = deg == 0f

    }
    class AnalogCameraAnimator(var shape:AnalogCameraShape,var view:AnalogCameraLikeView) {
        var animated = false
        fun update() {
            if(animated) {
                shape.update()
                if(shape.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex: Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(shape.handleTap(x,y)) {
                animated = true
                view.postInvalidate()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            shape.draw(canvas,paint)
        }
    }
    class AnalogCameraRenderer(var view:AnalogCameraLikeView,var time:Int = 0) {
        var animator:AnalogCameraAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = AnalogCameraAnimator(AnalogCameraShape(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object{
        fun create(activity:Activity) {
            val view = AnalogCameraLikeView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}