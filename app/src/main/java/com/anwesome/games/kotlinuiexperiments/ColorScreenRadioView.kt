package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 23/09/17.
 */
class ColorScreenRadioView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ColorScreenRadioRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean{
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class ColorScreenRadio(var w:Float,var h:Float,var rx:Float = w/20,var ry:Float = 19*h/20-w/20,var r:Float = w/20,var orx:Float = rx,var state:ColorScreenRadioState = ColorScreenRadioState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            rx = orx+(9*w/10)*state.scale
            canvas.drawRect(RectF(0f,0f,w*state.scale,0.8f*h),paint)
            canvas.drawLine(0f,ry,0.9f*w*state.scale,ry,paint)
            canvas.drawCircle(rx,ry,r,paint)

        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=rx-r && x<=rx+r && y>=ry-r && y<=ry+r
    }
    data class ColorScreenRadioState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                dir = 0f
                scale = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
    class ColorScreenRadioAnimator(var screen:ColorScreenRadio,var view:ColorScreenRadioView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                screen.update()
                if(screen.stopped()) {
                    animated = false
                }
                try{
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            screen.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(screen.handleTap(x,y) && !animated) {
                screen.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class ColorScreenRadioRenderer {
        var time = 0
        var animator:ColorScreenRadioAnimator?=null
        fun render(canvas: Canvas,paint:Paint,view:ColorScreenRadioView) {
            if(time == 0) {
                paint.color = Color.parseColor("#FDD835")
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ColorScreenRadioAnimator(ColorScreenRadio(w,h),view)
                paint.strokeWidth = Math.min(w,h)/40
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
        fun create(activity: Activity) {
            var view = ColorScreenRadioView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}