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

/**
 * Created by anweshmishra on 24/08/17.
 */
class PiePolygonalView(ctx:Context,var n:Int = 3):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = PiePolygonalRenderer(this)
    override fun onDraw(canvas:Canvas) {
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
    data class PiePolygonal(var w:Float,var h:Float,var n:Int,var scale:Float = 0.0f,var r:Float = Math.min(w,h)/10) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            var deg = 360.0f/n
            for(i in 1..n) {
                canvas.save()
                canvas.rotate(i*deg)
                var x_gap =  (w/3*Math.sin((deg/2)*Math.PI/180)).toFloat()
                var y_gap = (w/3*Math.cos((deg/2)*Math.PI/180)).toFloat()
                canvas.drawLine(-x_gap*scale,y_gap,x_gap*scale,y_gap,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float) = x>=w/2-r && x<=w/2+r && y>=h/2-r && y<=h/2+r
    }
    class PiePolygonalRenderer(var view:PiePolygonalView) {
        var animator = PiePolygonalAnimator(this)
        var time = 0
        var piePolygonal:PiePolygonal?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                piePolygonal = PiePolygonal(w,h,view.n)
                paint.color = Color.parseColor("#0288D1")
                paint.strokeWidth = Math.min(w,h)/50
                paint.strokeCap = Paint.Cap.ROUND
            }
            piePolygonal?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            if(piePolygonal?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
        fun update(scale:Float) {
            piePolygonal?.scale = scale
            view.postInvalidate()
        }
    }
    class PiePolygonalAnimator(var renderer:PiePolygonalRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animated = false
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        var dir = 0
        var reverseAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        init {
            anim.addUpdateListener(this)
            anim.addListener(this)
            reverseAnim.addUpdateListener(this)
            reverseAnim.addListener(this)
            reverseAnim.duration = 500
            anim.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            renderer.update(vf.animatedValue as Float)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                dir = (dir+1)%2
                animated = false
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                when(dir) {
                    0 -> anim.start()
                    1 -> reverseAnim.start()
                }
            }
        }
    }
    companion object {
        fun create(activity:Activity,vararg n:Int) {
            var view = PiePolygonalView(activity)
            if(n.size == 1) {
                view.n = Math.max(n[0],view.n)
            }
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}