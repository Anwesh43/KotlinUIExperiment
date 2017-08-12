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
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 12/08/17.
 */
class TickBoxSwitchView(ctx:Context,var n:Int = 5):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
    data class TickBox(var x:Float,var y:Float,var size:Float,var scale:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.WHITE
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            paint.color = Color.parseColor("#1976D2")
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
            paint.color = Color.WHITE
            paint.strokeWidth = size/15
            canvas.save()
            canvas.rotate(-45.0f)
            canvas.drawLine(0.0f,0.0f,0.0f,-size/6,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(60.0f)
            canvas.drawLine(0.0f,0.0f,0.0f,-size/3,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x - size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
    class TickBoxRenderer(var v:TickBoxSwitchView) {
        var time = 0
        var animator = TickBoxAnimController(this)
        var controller:TickBoxRenderController?=null
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0){
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var size = w/(2*v.n+1)
                var x = 3*size/2
                var tickBoxes:ConcurrentLinkedQueue<TickBox> = ConcurrentLinkedQueue()
                for(i in 0..v.n) {
                    tickBoxes.add(TickBox(x,h/2,size/2))
                    x += 2*size
                }
                controller = TickBoxRenderController(tickBoxes)
            }
            controller?.draw(canvas,paint)
            time++
        }
        fun update(factor: Float) {
            controller?.update(factor)
            v.postInvalidate()
        }
        fun stopUpdating() {
            controller?.stopUpdating()
        }
        fun handleTap(x:Float,y:Float) {
            if(controller?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
    }
    class TickBoxRenderController(var tickBoxes:ConcurrentLinkedQueue<TickBox>) {
        var prev:TickBox?=null
        var curr:TickBox?=null
        fun draw(canvas:Canvas,paint:Paint) {
            tickBoxes.forEach{ tickBox ->
                tickBox.draw(canvas,paint)
            }
        }
        fun update(factor:Float) {
            prev?.update(factor)
            curr?.update(1-factor)
        }
        fun stopUpdating() {
            prev = curr
            curr = null
        }
        fun handleTap(x:Float,y:Float):Boolean {
            if(curr == null) {
                tickBoxes.forEach { tickBox ->
                    if (tickBox.handleTap(x, y)) {
                        curr = tickBox
                        return true
                    }
                }
            }
            return false
        }
    }
    class TickBoxAnimController(var renderer:TickBoxRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animated = false
        var startAnim:ValueAnimator = ValueAnimator.ofFloat(0.0f,1.0f)
        init {
            startAnim.addUpdateListener(this)
            startAnim.addListener(this)
            startAnim.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            renderer.update(vf.animatedValue as Float)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                animated = false
                renderer.stopUpdating()
            }
        }
        fun start() {
            if(!animated) {
                startAnim.start()
                animated = true
            }
        }
    }
}