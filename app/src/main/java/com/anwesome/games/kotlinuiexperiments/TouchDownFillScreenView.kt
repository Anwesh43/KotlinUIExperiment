package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 26/09/17.
 */
class TouchDownFillScreenView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = TouchDownFillRenderer(this)
    val touchDownHandler = TouchDownHandler(renderer)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        touchDownHandler.handleTouch(event)
        return true
    }
    data class TouchDownFillScreen(var w:Float,var h:Float,var state:TouchDownFillState = TouchDownFillState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,0.9f*h)
            paint.strokeWidth = w/60
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,w/20,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w/20,-w/20,w/20,w/20),0f,360f*state.scale,true,paint)
            canvas.restore()
            canvas.drawRect(RectF(0f,0f,w,0.8f*h*state.scale),paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating(dir:Float) {
            state.startUpdating(dir)
        }
        fun stopped():Boolean = state.dir == 0f
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2 - w/20 && x<=w/2+w/20 && y>=0.9f*h-w/20 && y<=0.9f*h+w/20
    }
    data class TouchDownFillState(var scale:Float = 0f,var dir:Float = 0f) {
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
        fun startUpdating(dir:Float) {
            this.dir = dir
        }
    }
    class TouchDownFillAnimator(var touchDownFillScreen:TouchDownFillScreen,var view:TouchDownFillScreenView,var animated:Boolean = false) {
        fun update(){
            if(animated) {
                touchDownFillScreen.update()
                if(touchDownFillScreen.stopped()) {
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
        fun draw(canvas:Canvas,paint:Paint) {
            touchDownFillScreen.draw(canvas,paint)
        }
        fun startCollapsing() {
            touchDownFillScreen.startUpdating(-1f)
            startUpdatingIfStopped()
        }
        fun startUpdatingIfStopped() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun handleTap(x:Float,y:Float):Boolean {
            if(touchDownFillScreen.handleTap(x,y)) {
                touchDownFillScreen.startUpdating(1f)
                startUpdatingIfStopped()
                return true
            }
            return false
        }
    }
    class TouchDownFillRenderer(var view:TouchDownFillScreenView,var time:Int = 0) {
        var animator:TouchDownFillAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = TouchDownFillAnimator(TouchDownFillScreen(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float):Boolean {
            return animator?.handleTap(x,y)?:false
        }
        fun startCollapsing() {
            animator?.startCollapsing()
        }
    }
    data class TouchDownHandler(var renderer:TouchDownFillRenderer,var down:Boolean = false) {
        fun handleTouch(event:MotionEvent) {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(!down) {
                        down = renderer.handleTap(event.x, event.y)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(down) {
                        renderer.startCollapsing()
                        down = false
                    }
                }
            }
        }
    }
    companion object {
        fun create(activity: Activity){
            var view = TouchDownFillScreenView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y))
        }
    }
}