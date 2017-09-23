package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 23/09/17.
 */
class ColorScreenRadioView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean{
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class ColorScreenRadio(var w:Float,var h:Float,var rx:Float = w/20,var ry:Float = 19*h/20-w/20,var r:Float = w/20,var orx:Float = rx,var state:ColorScreenRadioState = ColorScreenRadioState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawRect(RectF(0f,0f,w*state.scale,9*h/10),paint)
            canvas.drawLine(0f,ry,0.9f*w*state.scale,ry,paint)
            canvas.drawCircle(rx,ry,r,paint)
            rx = orx+(9*w/10)*state.scale
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=rx-r && x<=rx+r && y>=ry-r && y<=ry+r
    }
    data class ColorScreenRadioState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                dir = 0f
                scale = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
}