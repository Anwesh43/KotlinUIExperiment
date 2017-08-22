package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 22/08/17.
 */
class CircularPlayRectView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CPRVRenderer(this)
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
    data class CircularPlayRect(var w:Float,var h:Float,var scale:Float = 0.0f) {
        var circularPlay:CircularPlay?=null
        init {
            circularPlay = CircularPlay(w/2,h/2,Math.min(w,h)/5)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#00838F")
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.scale(scale,1.0f)
            canvas.drawRect(RectF(-w/2,-h/2,w/2,h/2),paint)
            canvas.restore()
            circularPlay?.draw(canvas,paint,scale)
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = circularPlay?.handleTap(x,y)?:false
    }
    data class CircularPlay(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            var path = Path()
            path.moveTo(-r/6,-r/6)
            path.lineTo(-r/6,r/6)
            path.lineTo(r/6,0.0f)
            paint.style = Paint.Style.STROKE
            paint.color = Color.WHITE
            canvas.drawPath(path,paint)
            canvas.save()
            canvas.scale(scale,scale)
            paint.style = Paint.Style.FILL
            canvas.drawPath(path, paint)
            canvas.restore()
            paint.color = Color.parseColor("#1565C0")
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y <= this.y+r
    }
    class CPRVRenderer(var view:CircularPlayRectView) {
        var time = 0
        var animator = CPRVAnimator(this)
        var circularPlayRect:CircularPlayRect?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                circularPlayRect = CircularPlayRect(canvas.width.toFloat(),canvas.height.toFloat())
            }
            circularPlayRect?.draw(canvas,paint)
            time++
        }
        fun update(scale:Float) {
            circularPlayRect?.update(scale)
            view.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(circularPlayRect?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
    }
    class CPRVAnimator(var renderer:CPRVRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var animated = false
        var dir = 0
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        var reverseAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        init {
            anim.addUpdateListener(this)
            reverseAnim.addListener(this)
            anim.addListener(this)
            reverseAnim.addUpdateListener(this)
            anim.duration = 500
            reverseAnim.duration = 500
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
        fun create(activity:Activity) {
            var view = CircularPlayRectView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}