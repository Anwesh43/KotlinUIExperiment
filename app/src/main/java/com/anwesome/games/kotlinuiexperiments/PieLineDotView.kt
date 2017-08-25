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
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 26/08/17.
 */
class PieLineDotView(ctx:Context,var n:Int=4):View(ctx) {
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
    data class LineDot(var radius:Float,var w:Float,var y:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(y,radius)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,radius,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawCircle(0.0f,0.0f,radius,paint)
            canvas.restore()
            canvas.drawLine(2.5f*radius,0.0f,2.5f*radius+(w-2.5f*radius)*scale,0.0f,paint)
            canvas.restore()
        }
    }
    data class PieLineDot(var w:Float,var h:Float,var n:Int,var x:Float = w/2,var y:Float = 9*h/10,var r:Float = h/10,var scale:Float = 0.0f) {
        var lineDots:ConcurrentLinkedQueue<LineDot> = ConcurrentLinkedQueue()
        init {
            var gap = (0.8f*h)/n
            var y = 3*gap/2
            for(i in 1..n) {
                var lineDot = LineDot(gap/2,w,y)
                lineDots.add(lineDot)
                y += 2*gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            lineDots.forEach { lineDot ->
                 lineDot.draw(canvas,paint,scale)
            }
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.translate(x,y)
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class PieLineDotRenderer(var view:PieLineDotView) {
        var time = 0
        var pieLineDot:PieLineDot?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                pieLineDot = PieLineDot(w,h,view.n)
            }
            pieLineDot?.draw(canvas,paint)
            time++
        }
        fun update(scale:Float) {
            pieLineDot?.scale = scale
            view.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(pieLineDot?.handleTap(x,y)?:false) {

            }
        }
    }
    class PieLineDotAnimator(var renderer:PieLineDotRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        var reverseAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        var animated = false
        var dir = 0
        init {
            anim.addUpdateListener(this)
            reverseAnim.addUpdateListener(this)
            anim.addListener(this)
            reverseAnim.addListener(this)
            anim.duration = 500
            reverseAnim.duration = 500
        }
        override fun onAnimationUpdate(vf: ValueAnimator) {
            renderer.update(vf.animatedValue as Float)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                animated = false
                dir = (dir+1)%2
            }
        }
        fun start() {
            if(!animated) {
                when(dir) {
                    0 -> anim.start()
                    1 -> reverseAnim.start()
                }
                animated = true
            }
        }
    }
}