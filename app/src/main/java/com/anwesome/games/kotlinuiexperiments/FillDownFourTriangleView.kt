package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 07/10/17.
 */
class FillDownFourTriangleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

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
                path.lineTo(w/3,h/3)
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
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
}