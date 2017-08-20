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
 * Created by anweshmishra on 20/08/17.
 */
class BoxPieLoaderView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = BoxPieRenderer(this)
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class BoxPieLoader(var h:Float,var w:Float,var scale:Float=0.0f) {
        var r = Math.min(w,h)/15
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            for(i in 0..4) {
                canvas.save()
                canvas.rotate(90.0f*i)
                canvas.drawLine(-(0.4f*w)*scale,-0.4f*h,(0.4f*w)*scale,0.4f*h,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.w/2-r && x<=this.w/2+r && y>=this.h/2-r && y<=this.h/2 +r
    }
    class BoxPieRenderer(var view:BoxPieLoaderView) {
        var time = 0
        var loader:BoxPieLoader?=null
        var animator = BoxPieAnimator(this)
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                loader = BoxPieLoader(h,w)
                paint.color = Color.parseColor("#0D47A1")
                paint.strokeWidth = Math.min(w,h)/50
            }
            loader?.draw(canvas,paint)
            time++
        }
        fun update(factor:Float) {
            loader?.update(factor)
            view.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(loader?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
    }
    class BoxPieAnimator(var renderer:BoxPieRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animated:Boolean = false
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        var reverseAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        var dir = 0
        init {
            anim.addUpdateListener(this)
            anim.addListener(this)
            anim.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
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
    companion object {
        var view:BoxPieLoaderView?=null
        fun create(activity: Activity) {
            if(view == null) {
                view = BoxPieLoaderView(activity)
                var size = DimensionsUtil.getDimension(activity)
                activity.addContentView(view, ViewGroup.LayoutParams(size.x, size.x))
            }
        }
        fun create(parent: ViewGroup) {
            if(view == null) {
                view = BoxPieLoaderView(parent.context)
                var size = DimensionsUtil.getDimension(parent.context as Activity)
                parent.addView(view, ViewGroup.LayoutParams(size.x, size.x))
            }
        }

    }
}