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
 * Created by anweshmishra on 23/08/17.
 */
class PieCornerBallMoverView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = PieCornerMoverRenderer(this)
    override fun onDraw(canvas:Canvas) {
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
    data class PieCornerBallMover(var w:Float,var h:Float,var r:Float=Math.min(w,h)/10,var scale:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#26A69A")
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.strokeWidth = r/8
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(i*90.0f+45.0f)
                canvas.drawCircle(0.0f,-h/3*scale,r,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.w/2-r && x<=this.w/2+r && y>=this.h/2 -r && y<=this.h/2+r
        fun update(scale:Float) {
            this.scale = scale
        }
    }
    class PieCornerMoverRenderer(var view:PieCornerBallMoverView) {
        var time = 0
        var animator = PieCornerMoverAnimator(this)
        var pieCornerBallMover:PieCornerBallMover?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                pieCornerBallMover = PieCornerBallMover(canvas.width.toFloat(),canvas.height.toFloat())
            }
            pieCornerBallMover?.draw(canvas,paint)
            time++
        }
        fun update(scale:Float) {
            pieCornerBallMover?.update(scale)
            view.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(pieCornerBallMover?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
    }
    class PieCornerMoverAnimator(var renderer:PieCornerMoverRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var anim = ValueAnimator.ofFloat(0.0f,1.0f)
        var animated = false
        var dir = 0
        var reverseAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        init {
            anim.addUpdateListener(this)
            reverseAnim.addUpdateListener(this)
            anim.addListener(this)
            reverseAnim.addListener(this)
            anim.duration = 500
            reverseAnim.duration = 500
        }
        override fun onAnimationUpdate(animator:ValueAnimator) {
            if(animated) {
                renderer.update(animator.animatedValue as Float)
            }
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                dir = (dir+1)%2
                animated = false
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
            var view = PieCornerBallMoverView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}