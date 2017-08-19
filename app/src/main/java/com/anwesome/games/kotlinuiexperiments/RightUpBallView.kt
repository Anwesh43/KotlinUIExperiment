package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
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
 * Created by anweshmishra on 19/08/17.
 */
class RightUpBallView(ctx:Context,var n:Int = 20):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RUPRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class RUPBall(var x:Float,var y:Float,var r:Float,var h:Float,var w:Float,var i:Int) {
        var origX = 0.0f
        var origY = 0.0f
        init{
            origX = x
            origY = y
        }

        fun setOrigX() {
            origX = x
        }
        fun draw(canvas: Canvas,paint:Paint) {
            paint.color = Color.parseColor("#f44336")
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun update(xscale:Float,yscale:Float) {
            x = origX + w*xscale
            y = origY - h*yscale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class RUPRenderer(var v:RightUpBallView) {
        var time = 0
        var curr:RUPBall?=null
        var prev:RUPBall?=null
        var index = 0
        var controller = AnimatorController(this)
        var balls:ConcurrentLinkedQueue<RUPBall> = ConcurrentLinkedQueue()
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                for(i in 0..v.n-1) {
                    var ball = RUPBall(-w/20,h/2,w/20,h/2,w/2,i)
                    balls.add(ball)
                    if(i == 0) {
                        curr = ball
                    }
                }
            }
            balls.forEach { ball ->
                ball.draw(canvas,paint)
            }
            if(time == 0) {
                controller.start()
            }
            time++
        }
        fun update(factor:Float) {
            curr?.update(factor,0.0f)
            prev?.update(0.0f,factor)
            v.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(prev?.handleTap(x,y)?:false) {
                index++
                var rightBalls = balls.filter{ ball ->
                    ball.i == index
                }
                if(rightBalls.size == 1) {
                    curr = rightBalls[0]
                }
                controller.start()
            }
        }
        fun handleUpAnimEnd() {
            curr?.setOrigX()
            balls.remove(prev)
            prev = curr
            curr = null
        }
    }
    class AnimatorController(var renderer:RUPRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animator = ValueAnimator.ofFloat(0.0f,1.0f)
        var animated = false
        init {
            animator.addUpdateListener(this)
            animator.addListener(this)
            animator.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            var factor = vf.animatedValue as Float
            renderer.update(factor)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                animated = false
                renderer.handleUpAnimEnd()
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = RightUpBallView(activity)
            activity.setContentView(view)
        }
    }
}