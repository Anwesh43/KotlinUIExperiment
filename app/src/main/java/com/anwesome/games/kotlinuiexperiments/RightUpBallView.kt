package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 19/08/17.
 */
class RightUpBallView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class RUPBall(var x:Float,var y:Float,var r:Float,var h:Float,var w:Float) {
        fun draw(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun update(xscale:Float,yscale:Float) {
            x = w*xscale
            y = h*yscale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class AnimatorController():AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animator = ValueAnimator.ofFloat(0.0f,1.0f)
        var animated = false
        init {
            animator.addUpdateListener(this)
            animator.addListener(this)
            animator.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            var factor = vf.animatedValue as Float
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                animated = false
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                animator.start()
            }
        }
    }
}