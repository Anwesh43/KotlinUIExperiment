package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 10/11/17.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
class FourSideWheelView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = FourSideWheelRenderer(view = this)
    var fourSideWheelOnClickListener:FourSideWheelOnClickListener ?= null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                renderer.handleTap()
            }
        }
        return true
    }
    fun addClickListener(clickListener:()->Unit) {
        fourSideWheelOnClickListener = FourSideWheelOnClickListener(clickListener)
    }
    data class FourSideWheel(var w:Float,var h:Float) {
        var state = FourSideWheelState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = Math.min(w,h)/60
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.parseColor("#1565C0")
            val r = Math.min(w,h)/3
            val r1 = 0.4f*Math.min(w,h)
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.rotate(state.deg)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,r*state.scale,paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = Math.min(w,h)/40
            for(i in 0..5) {
                canvas.save()
                canvas.rotate(60f*i)
                canvas.drawArc(RectF(-r1,-r1,r1,r1),15f,20f,false,paint)
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
    data class FourSideWheelState(var dir:Float = 0f,var deg:Float = 0f,var prevDeg:Float = 0f,var scale:Float = 0f) {
        fun update() {
            deg += 90f*0.1f*dir
            scale = Math.sin(2*(deg-prevDeg)*Math.PI/180).toFloat()
            if(Math.abs(prevDeg - deg) >= 90) {
                deg = prevDeg +90f
                prevDeg = deg
                dir = 0f
                scale = 0f
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    class FourSideWheelAnimator(var fourSideWheel:FourSideWheel,var view:FourSideWheelView){
        var animated:Boolean = false
        fun update() {
            if(animated) {
                fourSideWheel.update()
                if(fourSideWheel.stopped()){
                    animated = false
                    view.fourSideWheelOnClickListener?.onClickListener?.invoke()
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
            fourSideWheel.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                fourSideWheel.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class FourSideWheelRenderer(var time:Int = 0,var view:FourSideWheelView) {
        var animator:FourSideWheelAnimator?=null
        fun handleTap() {
            animator?.handleTap()
        }
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = FourSideWheelAnimator(FourSideWheel(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
    }
    companion object {
        fun create(activity:Activity):View {
            val view = FourSideWheelView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
            return view
        }
    }
    data class FourSideWheelOnClickListener(var onClickListener:()->Unit)
}