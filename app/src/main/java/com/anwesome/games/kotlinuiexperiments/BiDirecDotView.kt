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
 * Created by anweshmishra on 28/08/17.
 */

class BiDirecDotView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer = BiDirecRenderer(this)
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    class BDDVAnimator(var renderer: BiDirecRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
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
                var factor = vf.animatedValue as Float
                renderer.update(factor,index)
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
            paint.color = Color.BLUE
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x, y)
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(-(w / 2) * scale2*(1-2*i), 0f, r, paint)
                canvas.save()
                canvas.scale(scale1, scale1)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(-(w / 2) * scale2*(1-2*i), 0f ,r, paint)
                canvas.restore()
                canvas.drawLine(-(w / 2) * scale2*(1-2*i), 0f, 0f, 0f, paint)
                canvas.restore()
            }
        }
        fun updateScale1(scale:Float) {
            this.scale1 = scale
        }
        fun updateScale2(scale:Float) {
            this.scale2 = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x +r  && y>=this.y-r && y<=this.y+r
    }
    class BiDirecRenderer(var view:BiDirecDotView) {
        var time = 0
        var bidirecdot:BiDirecDot?=null
        var animator = BDDVAnimator(this)
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                bidirecdot = BiDirecDot(w/2,h/2,2*h/5,w-4*h/5)
            }
            bidirecdot?.draw(canvas,paint)
            time++
        }
        fun update(scale:Float,i:Int) {
            when(i) {
                0 -> bidirecdot?.updateScale1(scale)
                1 -> bidirecdot?.updateScale2(scale)
            }

            view.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(bidirecdot?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = BiDirecDotView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y/12))
        }
    }
}