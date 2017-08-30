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
 * Created by anweshmishra on 30/08/17.
 */
class ArrowPieMoverView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ArrowPieMoverRenderer(this)
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
    data class ArrowPieMover(var w:Float,var h:Float,var scale:Float = 0.0f,var r:Float = Math.min(w,h)/15) {
        var drawArrow: (Canvas, Paint, Float, Float, Float) -> Unit = { canvas, paint, w, size, scale ->
            paint.strokeWidth = w/65
            for (i in 0..3) {
                paint.color = Color.WHITE
                canvas.save()
                canvas.rotate(90f * i + 45)
                canvas.save()
                canvas.translate(0f, -(w) * scale)
                canvas.rotate(180f*scale)
                canvas.drawLine(0f, 0f, 0f, -size, paint)
                for (j in 0..1) {
                    canvas.save()
                    canvas.translate(0f, -size)
                    canvas.rotate((2 * j - 1) * 45f)
                    canvas.drawLine(0f, 0f, 0f, size / 4, paint)
                    canvas.restore()
                }
                canvas.restore()
                canvas.restore()
            }
        }
        var drawPie: (Canvas, Paint, Float, Float) -> Unit = { canvas, paint, r, scale ->
            paint.color = Color.parseColor("#1976D2")
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f, 0f, r, paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r, -r, r, r), 0f, 360 * scale, true, paint)
        }

        fun draw(canvas: Canvas, paint: Paint) {
            canvas.save()
            canvas.translate(w / 2, h / 2)
            paint.strokeWidth = Math.min(w, h) / 35
            paint.strokeCap = Paint.Cap.ROUND
            drawPie(canvas, paint, r, scale)
            drawArrow(canvas, paint, Math.min(w, h)/2, r, scale)
            canvas.restore()
        }

        fun update(scale: Float) {
            this.scale = scale
        }

        fun handleTap(x: Float, y: Float): Boolean = x >= this.w / 2 - r && x <= this.w / 2 + r && y >= this.h / 2 - r && y <= this.h / 2 + r
    }
    class ArrowPieMoverRenderer(var view:View) {
        var animator = ArrowPieMoverAnimator(this)
        var render:Int = 0
        var arrowPieMover:ArrowPieMover?=null
        fun update(scale:Float) {
            arrowPieMover?.update(scale)
            view.postInvalidate()
        }
        fun render(canvas:Canvas,paint:Paint) {
            if(render == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                arrowPieMover = ArrowPieMover(w,h)
            }
            arrowPieMover?.draw(canvas,paint)
            render++
        }
        fun handleTap(x:Float,y:Float) {
            if(arrowPieMover?.handleTap(x,y)?:false) {
                animator.start()
            }
        }
    }
    class ArrowPieMoverAnimator(var renderer:ArrowPieMoverRenderer):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var anim = ValueAnimator.ofFloat(0f,1f)
        var reverseAnim = ValueAnimator.ofFloat(1f,0f)
        var mode = 0
        var animating = false
        init {
            anim.addUpdateListener(this)
            reverseAnim.addUpdateListener(this)
            anim.addListener(this)
            reverseAnim.addListener(this)
            anim.duration = 500
            reverseAnim.duration = 500
        }
        override fun onAnimationUpdate(vf:ValueAnimator) {
            var factor = vf.animatedValue as Float
            renderer.update(factor)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animating) {
                mode = (mode + 1) % 2
                animating = false
            }
        }
        fun start() {
            if(!animating) {
                when (mode) {
                    0 -> anim.start()
                    1 -> reverseAnim.start()
                }
                animating = true
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = ArrowPieMoverView(activity)
            var size:Point = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}