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
}