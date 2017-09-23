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
    data class ColorScreenRadio(var w:Float,var h:Float,var rx:Float = w/20,var ry:Float = 19*h/20-w/20,var r:Float = w/20,var orx:Float = rx) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawRect(RectF(0f,0f,w,9*h/10),paint)
            canvas.drawLine(0f,ry,0.9f*w,ry,paint)
            canvas.drawCircle(rx,ry,r,paint)
            rx = orx+(9*w/10)
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
        fun handleTap(x:Float,y:Float):Boolean = x>=rx-r && x<=rx+r && y>=ry-r && y<=ry+r
    }
}