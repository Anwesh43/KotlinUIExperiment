package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 01/10/17.
 */
class ClockTapView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ClockTapRenderer(this)
    var listener:OnTimeChangeListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class TapClock(var w:Float,var h:Float,var size:Float = 2*Math.min(w,h)/5,var hDeg:Float = 0f,var state:TapClockState = TapClockState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = size/25
            canvas.drawCircle(0f,0f,size,paint)
            paint.strokeWidth = size/20
            drawMinuteHand(canvas,paint)
            paint.strokeWidth = size/10
            drawHourHand(canvas,paint)
            canvas.restore()
        }
        private fun drawMinuteHand(canvas: Canvas,paint: Paint) {
            canvas.save()
            canvas.rotate(360f*state.scale)
            canvas.drawLine(0f,0f,0f,-2*size/3,paint)
            canvas.restore()
        }
        private fun drawHourHand(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.rotate(hDeg+30f*state.scale)
            canvas.drawLine(0f,0f,0f,-2*size/5,paint)
            canvas.restore()
        }
        fun update() {
            state.update({
                hDeg+=30
                if(hDeg >= 360f) {
                    hDeg = 0f
                }
            })
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-size && x<=w/2+size && y>=h/2-size && y<=h/2+size
    }
    data class TapClockState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update(onstop:()->Unit) {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 0f
                onstop()
            }
        }
        fun startUpdating() {
            dir = 1f
        }
        fun stopped():Boolean = dir == 0f
    }
    class ClockTapAnimator(var tapClock:TapClock,var view:ClockTapView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                tapClock.update()
                if(tapClock.stopped()) {
                    animated = false
                    view.listener?.timeChangeListener?.invoke((tapClock.hDeg/30).toInt())
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
            tapClock.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && tapClock.handleTap(x,y)) {
                tapClock.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class ClockTapRenderer(var view:ClockTapView,var time:Int = 0) {
        var animator:ClockTapAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ClockTapAnimator(TapClock(w,h),view)
                paint.color = Color.parseColor("#3949AB")
                paint.strokeCap = Paint.Cap.ROUND
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
        fun create(activity:Activity,vararg listeners:(Int)->Unit) {
            val view = ClockTapView(activity)
            val size = DimensionsUtil.getDimension(activity)
            if(listeners.size == 1) {
                view.listener = OnTimeChangeListener(listeners[0])
            }
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
    data class OnTimeChangeListener(var timeChangeListener:(Int)->Unit)
}