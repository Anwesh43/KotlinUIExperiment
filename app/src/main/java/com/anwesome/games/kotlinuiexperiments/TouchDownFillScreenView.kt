package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 26/09/17.
 */
class TouchDownFillScreenView(ctx:Context):View(ctx) {
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
    data class TouchDownFillScreen(var w:Float,var h:Float,var state:TouchUpFillState = TouchUpFillState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,0.9f*h)
            paint.strokeWidth = w/60
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,w/20,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w/20,-w/20,w/20,w/20),0f,360f*state.scale,true,paint)
            canvas.restore()
            canvas.drawRect(RectF(0f,0f,w,0.8f*h*state.scale),paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating(dir:Float) {
            state.startUpdating(dir)
        }
        fun stopped():Boolean = state.dir == 0f
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2 - w/20 && x<=w/2+w/20 && y>=0.9f*h-w/20 && y<=0.9f*h+w/20
    }
    data class TouchUpFillState(var scale:Float = 0f,var dir:Float = 0f) {
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
        fun startUpdating(dir:Float) {
            this.dir = dir
        }
    }
}