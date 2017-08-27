package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 28/08/17.
 */

class BiDirecDotView(ctx:Context):View(ctx) {
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
    class BDDVAnimator():AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        var currAnim = anim
        var reverseAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        var mode = 0
        var index = 0
        var animated = false
        init {
            anim.addUpdateListener(this)
            anim.addListener(this)
            reverseAnim.addUpdateListener(this)
            reverseAnim.addListener(this)
            anim.duration = 500
            reverseAnim.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            if(animated) {

            }
        }
        override fun onAnimationEnd(animation: Animator) {
            if(animated) {
                if(index == 0) {
                    currAnim.start()
                    index++
                }
                else {
                    mode = (mode+1)%2
                    animated = false
                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                when (mode) {
                    0 -> {
                        currAnim = anim
                        anim.start()
                    }
                    1 -> {
                        currAnim = reverseAnim
                        reverseAnim.start()
                    }
                }
            }
        }
    }
    data class BiDirecDot(var x:Float,var y:Float,var r:Float,var w:Float,var scale1:Float=0.0f,var scale2:Float =0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..2) {
                canvas.save()
                canvas.translate(x, y)
                canvas.scale(i-1f,0f)
                canvas.drawCircle(-(w / 2) * scale2, (-w / 2) * scale2, r, paint)
                canvas.save()
                canvas.scale(scale1, scale1)
                canvas.drawCircle(-(w / 2) * scale2, (-w / 2) * scale2, r, paint)
                canvas.restore()
                canvas.drawLine(-w / 2 * scale2, 0f, 0f, 0f, paint)
                canvas.restore()
            }
        }
        fun update(scale1: Float,scale2:Float) {
            this.scale1 = scale1
            this.scale2 = scale2
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x +r  && y>=this.y-r && y<=this.y+r
    }
}