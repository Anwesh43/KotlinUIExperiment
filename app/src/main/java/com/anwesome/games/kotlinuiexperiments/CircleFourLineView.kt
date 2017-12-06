package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 06/12/17.
 */
import android.graphics.*
import android.content.*
import android.view.*

class CircleFourLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CircleFourLineRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class CircleFourLine(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.strokeWidth = r/12
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(r-(0.8f*r*scale),0f,r,0f,paint)
                canvas.restore()
            }
            canvas.drawCircle(0f,0f,r/5,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r/5,-r/5,r/5,r/5),0f,360f,true,paint)
            canvas.restore()
        }
    }
    data class CircleFourLineContainer(var w:Float,var h:Float) {
        var state = CircleFourLineState()
        var circleFourLine = CircleFourLine(w/2,h/2,Math.min(w,h)/3)
        fun draw(canvas:Canvas,paint:Paint) {
            circleFourLine.draw(canvas,paint,state.scale)
        }
        fun update(stopcb:()->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class CircleFourLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:()->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
        }
    }
    data class CircleFourLineAnimator(var container:CircleFourLineContainer,var view:CircleFourLineView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                container.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class CircleFourLineRenderer(var view:CircleFourLineView,var time:Int = 0) {
        var animator:CircleFourLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = CircleFourLineAnimator(CircleFourLineContainer(w,h),view)
                paint.color = Color.parseColor("#311B92")
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
}