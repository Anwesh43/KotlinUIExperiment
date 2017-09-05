package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 06/09/17.
 */
class DustbinFillButtonView(ctx:Context):View(ctx) {
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
    data class DustbinFill(var w:Float,var h:Float,var state:DustbinState= DustbinState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#9E9E9E")
            drawBin(canvas, paint,Color.parseColor("#757575"))
            paint.color = Color.parseColor("#1565C0")
            canvas.save()
            var clipPath = Path()
            clipPath.addRect(RectF(w/10,7*h/10-(6*h/10+h/30),7*w/10,7*h/10+h/30),Path.Direction.CCW)
            canvas.clipPath(clipPath)
            drawBin(canvas,paint,paint.color)
            canvas.restore()
        }
        private fun drawBin(canvas: Canvas,paint: Paint,color:Int) {
            var path = Path()
            path.moveTo(w/5,7*h/10)
            path.lineTo(w/10,h/10)
            path.lineTo(7*w/10,h/10)
            path.lineTo(4*w/10,7*h/10)
            path.lineTo(w/5,7*h/10)
            canvas.drawPath(path,paint)
            drawEllipse(canvas,paint,w/2,7*h/10,3*w/20,h/30)
            paint.color = color
            drawEllipse(canvas,paint,w/2,h/10,3*w/10,h/30)
        }
        private fun drawEllipse(canvas: Canvas,paint:Paint,x1:Float,y1:Float,r1:Float,r2:Float) {
            canvas.drawArc(RectF(x1-r1,y1-r2,x1+r1,y1+r1),0f,360f,true,paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdate()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class DustbinState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir * 0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdate() {
            dir = 1f-2*scale
        }
    }
    class DustbinAnimator(var dustbin:DustbinFill,var view:DustbinFillButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                dustbin.update()
                if(dustbin.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            dustbin.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                dustbin.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
}