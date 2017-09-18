package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 18/09/17.
 */
class PieLoaderDirectionView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = PieDirecRenderer()
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class PieDirectionLoader(var w:Float,var h:Float,var state:PieDirecState = PieDirecState()) {
        fun draw(canvas: Canvas,paint:Paint) {
            val color = Color.parseColor("00ACC1")
            paint.color = color
            canvas.save()
            canvas.translate(w / 2, h / 2)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f, 0f, w / 5, paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w / 5, -w / 5, w / 5, w / 5), 0f, 360f, true, paint)
            canvas.restore()
            canvas.drawLine(-w / 20, 4 * h / 5, -w / 20 + (w), 4 * h / 5, paint)
            canvas.drawCircle(-w / 20 + (w), 4 * h / 5, w / 40, paint)
            paint.color = Color.argb(150, Color.red(color), Color.green(color), Color.blue(color))
            canvas.drawCircle(-w/20+(w),4*h/5,w/30,paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-w/10 && x<=w/2+w/10 && y>=h/2-w/10 && y<=h/2+w/10
    }
    data class PieDirecState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1f - 2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class PieDirecAnimator(var pieDirectionLoader: PieDirectionLoader,var view:PieLoaderDirectionView,var animated:Boolean = false) {
        fun draw(canvas: Canvas, paint: Paint) {
            pieDirectionLoader.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                pieDirectionLoader.update()
                if(pieDirectionLoader.stopped()) {
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
        fun handleTap(x:Float,y:Float) {
            if(pieDirectionLoader.handleTap(x,y) && !animated) {
                pieDirectionLoader.startUpdating()
            }
        }
    }
    class PieDirecRenderer {
        var time = 0
        var animator:PieDirecAnimator?=null
        fun render(canvas: Canvas,paint:Paint,view:PieLoaderDirectionView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = PieDirecAnimator(PieDirectionLoader(w,h),view)
            }
            animator?.draw(canvas, paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
}