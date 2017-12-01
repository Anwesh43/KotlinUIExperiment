package com.anwesome.games.kotlinuiexperiments

import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 01/12/17.
 */

class StackRectsView(ctx:Context):View(ctx) {
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
    data class StackRect(var x:Float,var y:Float,var size:Float,var dest:Float,var py:Float=y) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class StackRectState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating() {
            dir = 1f - 2*scale
        }
    }
}