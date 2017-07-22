package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 22/07/17.
 */
class DotLineSwitchView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class DLSVRenderer {
        var time = 0
        var drawingController:DLSVDrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:DotLineSwitchView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.strokeWidth = w/60
                drawingController = DLSVDrawingController(DotLine(w/2,h/2,w/3),v)
            }
            drawingController?.draw(canvas,paint)
            drawingController?.animate()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.startAnimation()
        }
    }
    class DLSVDrawingController(var dotLine:DotLine,var v:DotLineSwitchView) {
        var animated = false
        var stateContainer = StateContainer()
        fun draw(canvas:Canvas,paint:Paint) {
            dotLine.draw(canvas,paint,stateContainer.scale)
        }
        fun animate() {
            if (animated) {
                stateContainer.update()
                if(stateContainer.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                } catch (ex: Exception) {

                }
            }
        }
        fun startAnimation() {
            if(!animated) {
                stateContainer.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    data class DotLine(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x,y)
                canvas.scale(2*i-1.0f,1.0f)
                paint.style = Paint.Style.STROKE
                canvas.drawCircle(-4*size/5,0.0f,size/5,paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(-4*size/5,0.0f,size/5,paint)
                canvas.drawLine(0.0f,0.0f,(3*size/5)*scale,0.0f,paint)
                canvas.restore()
            }
        }
    }
    class StateContainer {
        var scale:Float = 0.0f
        var dir = 0
        fun update() {
            scale += 0.2f*dir;
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun startUpdating() {
            if(scale <= 0) {
                dir = 1
            }
            else if(scale >= 1) {
                dir = -1
            }
        }
    }
}