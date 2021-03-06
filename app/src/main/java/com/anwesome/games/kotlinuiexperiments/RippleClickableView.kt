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
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 14/09/17.
 */
class RippleClickableView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RippleClickableRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class RippleClickable(var x:Float,var y:Float,var r:Float,var state:RippleClickableState = RippleClickableState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(state.scale,state.scale)
            paint.style = Paint.Style.STROKE
            paint.color = Color.argb((255*state.scale).toInt(),255,255,255)
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(RectF(-r/10,-r/10,r/10,r/10),r/40,r/40,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class RippleClickableState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 4.5f
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class RippleClickableAnimator(var clickableView: RippleClickableView,var clickables:ConcurrentLinkedQueue<RippleClickable> = ConcurrentLinkedQueue(),var isAnimated:Boolean = false) {
        fun draw(canvas: Canvas,paint:Paint) {
            clickables.forEach { clickable ->
                clickable.draw(canvas,paint)
            }
        }
        fun update() {
            if(isAnimated) {
                clickables.forEach { rippleClickable ->
                    rippleClickable.update()
                    if(rippleClickable.stopped()) {
                        clickables.remove(rippleClickable)
                        if(clickables.size == 0) {
                            isAnimated = false
                        }
                    }
                }
                try {
                    Thread.sleep(10)
                    clickableView.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float,r:Float) {
            val rippleClickable = RippleClickable(x,y,r)
            clickables.add(rippleClickable)
            if(!isAnimated) {
                isAnimated = true
                clickableView.postInvalidate()
            }
        }
    }
    class RippleClickableRenderer {
        var r = 0f
        var time = 0
        var rippleClickableAnimator:RippleClickableAnimator? = null
        fun render(canvas: Canvas,paint:Paint,view:RippleClickableView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                r = Math.min(w,h)/15
                rippleClickableAnimator = RippleClickableAnimator(view)
                paint.color = Color.WHITE
                paint.strokeWidth = r/7
            }
            rippleClickableAnimator?.draw(canvas,paint)
            rippleClickableAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            rippleClickableAnimator?.handleTap(x,y,r)
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = RippleClickableView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y))
        }
    }
}