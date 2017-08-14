package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/08/17.
 */
class StackButton(ctx:Context,var color:Int,var text:String):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class StackButtonShape(var x:Float,var y:Float,var w:Float,var h:Float,var color:Int,var text:String) {
        fun draw(canvas: Canvas,paint:Paint) {
            paint.textSize = h/3
            paint.color = color
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(0.0f,0.0f,w,h),paint)
            paint.color = Color.WHITE
            canvas.drawText(text,-paint.measureText(text)/2,0.0f,paint)
            canvas.restore()
        }
    }
    data class CloseButton(var x:Float,var y:Float,var size:Float,var scale:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = size/10
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.WHITE
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x, y)
                canvas.rotate(i*90.0f+45.0f)
                canvas.drawLine(0.0f,-size/2,0.0f,size/2,paint)
                canvas.save()
                canvas.scale(scale,scale)
                paint.color = Color.argb(150,0,0,0)
                canvas.drawCircle(0.0f,0.0f,size/2,paint)
                canvas.restore()
                canvas.restore()
            }
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2 && scale == 0.0f
    }
    class StackButtonRenderer(var v:StackButton) {
        var stackButtonShape:StackButtonShape?=null
        var closeButton:CloseButton?=null
        var translateAnimator:TranslateXAnimator?=null
        var scaleAnimator = ScaleUpAnimator(this,{translateAnimator?.start()})

        var time = 0
        fun draw(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                stackButtonShape = StackButtonShape(0.0f,0.0f,w,h,v.color,v.text)
                closeButton = CloseButton(0.9f*w,0.2f*h,0.1f*Math.min(w,h))
                translateAnimator = TranslateXAnimator(v,w)
            }
            stackButtonShape?.draw(canvas,paint)
            closeButton?.draw(canvas,paint)
            time++
        }
        fun update(scale:Float) {
            closeButton?.update(scale)
            v.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(closeButton?.handleTap(x,y)?:false) {

            }
        }
    }
    class ScaleUpAnimator(var renderer:StackButtonRenderer,var cb:()->Unit):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animated = false
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        init {
            anim.addUpdateListener(this)
            anim.addListener(this)
            anim.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            renderer.update(vf.animatedFraction)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                animated = false
                cb()
            }
        }
        fun start() {
            if(!animated) {
                anim.start()
                animated = true
            }
        }
    }
    class TranslateXAnimator(var v:StackButton,var w:Float):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener  {
        var anim = ValueAnimator.ofFloat(0.0f,w)
        var animated = false
        init {
            anim.addUpdateListener(this)
            anim.addListener(this)
            anim.duration = 500
        }
        fun start() {
            if(!animated) {
                animated = true
                anim.start()
            }
        }

        override fun onAnimationUpdate(animation: ValueAnimator) {
            v.x = (animation.animatedValue as Float)
        }
        override fun onAnimationEnd(animation: Animator) {
            if(animated) {
                animated = false
            }

        }
    }
}