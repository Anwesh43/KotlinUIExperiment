package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 06/08/17.
 */

class GreenToRedButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = GTRRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class GreenToRedButton(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45*scale)
            paint.color = Color.rgb((255*scale).toInt(),(255*(1-scale)).toInt(),0)
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            paint.color = Color.BLACK
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(i*90.0f)
                canvas.drawLine(0.0f,-size/3,0.0f,size/3,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
    class GTRAnimListener(var renderer:GTRRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var dir = 0
        var animated = false
        var startAnim = ValueAnimator.ofFloat(0.0f,1.0f)
        var endAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        override fun onAnimationUpdate(vf:ValueAnimator) {
            var factor = vf.animatedValue as Float
            renderer.update(factor)
        }
        override fun onAnimationEnd(animation: Animator?) {
            if(animated) {
                dir = when(dir) {
                    1 -> 0
                    0 -> 1
                    else -> dir
                }
                animated = false
            }
        }
        fun start() {
            if(!animated) {
                when(dir) {
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
    class GTRRenderer(var v:GreenToRedButtonView) {
        var time = 0
        var scale:Float = 0.0f
        var gtrButton:GreenToRedButton?=null
        var listener = GTRAnimListener(this)
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                gtrButton = GreenToRedButton(w/2,h/2,Math.min(w,h)*0.4f)
            }
            gtrButton?.draw(canvas,paint,scale)
            time++
        }
        fun update(factor:Float) {
            scale = factor
            v.postInvalidate()
        }
        fun handleTap() {
            listener.start()
        }
    }
    companion object {
        fun create(activity: Activity) {
            var view = GreenToRedButtonView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
}