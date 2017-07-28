package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 28/07/17.
 */
class IClassButton(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class ICBDrawingHandler(var v:IClassButton) {
        var time = 0
        var iscaleup:IScaleUP?=null
        fun draw(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var size = 2*Math.min(w,h)/3
                iscaleup = IScaleUP(w/2,h/2,size)
            }
            iscaleup?.draw(canvas,paint)
            time++
        }
        fun update(factor:Float) {
            iscaleup?.scale = factor
            v.postInvalidate()
        }
        fun handleTap() {

        }
    }
    data class IScaleUP(var x:Float,var y:Float,var size:Float,var scale:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            var updateScale = 0.2f+0.8f*scale
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(360*scale)
            canvas.scale(updateScale,updateScale)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            paint.strokeWidth = size/15
            paint.color = Color.WHITE
            canvas.drawLine(0.0f,-size/3,0.0f,size/3,paint)
            canvas.save()
            canvas.translate(-2*size/5,0.0f)
            canvas.drawRect(RectF(-size/15,-size/15,size/15,size/15),paint)
            canvas.restore()
            canvas.restore()
        }
    }
    class ISBAnimHandler(var handler:ICBDrawingHandler):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var startAnim = ValueAnimator.ofFloat(0.0f,1.0f)
        var endAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        var state = 0
        var animated = false
        init {
            startAnim.duration = 500
            endAnim.duration = 500
            startAnim.addUpdateListener(this)
            endAnim.addUpdateListener(this)
            startAnim.addListener(this)
            endAnim.addListener(this)
        }
        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            if(animated) {
                handler.update(valueAnimator.animatedValue as Float)
            }
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                state = when(state) {
                    0 -> 1
                    else -> 0
                }
                animated = false
            }
        }
        fun start() {
            if(!animated) {
                when (state) {
                    0 -> {
                        startAnim.start()
                    }
                    1 -> {
                        endAnim.start()
                    }
                }
                animated = true
            }
        }
    }
}