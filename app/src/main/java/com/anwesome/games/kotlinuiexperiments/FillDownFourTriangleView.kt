package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 07/10/17.
 */
class FillDownFourTriangleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = FillDownFourTriangleRenderer(this)
    var clickListener:FDTClickListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class FillDownFourTriangle(var w:Float,var h:Float,var state:FillDownTriangleState = FillDownTriangleState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.FILL
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                val path = Path()
                path.moveTo(-w/3,-h/3)
                path.lineTo(0f,-h/3+h/3*state.scale)
                path.lineTo(w/3,-h/3)
                canvas.drawPath(path,paint)

                canvas.restore()
            }
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,w/15,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w/15,-w/15,w/15,w/15),0f,360f*state.scale,true,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-w/15 && x<=w/2+w/15 && y>=w/2-w/15 && y<=w/2+w/15
    }
    data class FillDownTriangleState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale = Math.sin(deg*Math.PI/180).toFloat()
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class FillDownTriangleAnimator(var fillDownFourTriangle:FillDownFourTriangle,var view:FillDownFourTriangleView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                fillDownFourTriangle.update()
                if(fillDownFourTriangle.stopped()) {
                    animated = false
                    view.clickListener?.onClickListener?.invoke()
                }
                try {
                    Thread.sleep(50)
                    view.postInvalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            fillDownFourTriangle.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && fillDownFourTriangle.handleTap(x,y)) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class FillDownFourTriangleRenderer(var view:FillDownFourTriangleView,var time:Int = 0) {
        var animator:FillDownTriangleAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = FillDownTriangleAnimator(FillDownFourTriangle(w,h),view)
                paint.color = Color.parseColor("#0D47A1")
                paint.strokeWidth = Math.min(w,h)/50
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity,vararg listeners:()->Unit) {
            val view = FillDownFourTriangleView(activity)
            if(listeners.size == 1) {
                view.clickListener = FDTClickListener(listeners[0])
            }
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
    data class FDTClickListener(var onClickListener:()->Unit)
}