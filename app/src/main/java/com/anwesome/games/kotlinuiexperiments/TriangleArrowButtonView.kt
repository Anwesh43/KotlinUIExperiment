package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 16/09/17.
 */
class  TriangleArrowButtonView(ctx:Context):View(ctx) {
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
    data class TriangleArrowButton(var x:Float,var y:Float,var l:Float,var r:Float,var state:TriangleArrowButtonState = TriangleArrowButtonState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            canvas.save()
            canvas.translate(x,y)
            for(i in 1..4) {
                canvas.save()
                canvas.rotate(90f*i*state.scale)
                canvas.translate(0f,l*state.scale)
                var path = Path()
                path.moveTo(-r/2,r/2)
                path.lineTo(r/2,r/2)
                path.lineTo(0f,-r/2)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class TriangleArrowButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class TriangleArrowButtonAnimator(var triangleButton:TriangleArrowButton,var view:TriangleArrowButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                triangleButton.update()
                if(triangleButton.stopped()) {
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
        fun startUpdating() {
            if(!animated) {
                triangleButton.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
}