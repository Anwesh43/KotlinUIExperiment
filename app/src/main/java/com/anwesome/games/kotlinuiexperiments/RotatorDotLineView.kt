package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class RotatorDotLineView(ctx:Context):View(ctx) {
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
    data class RotatorLine(var x:Float,var y:Float,var deg:Float=0f,var size:Float,var prevDeg:Float = 0f) {
        var state = RotatorLineState()
        fun update() {
            state.startUpdating()
        }
        fun startUpdating(finalDeg:Float) {
            deg = finalDeg - prevDeg
        }
        fun stopped():Boolean {
            val stopCondition = state.stopped()
            if(stopCondition) {
                prevDeg += deg
            }
            return stopCondition
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(prevDeg+deg*state.scale)
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
        }
    }
    data class RotatorLineState(var dir:Float= 0f,var scale:Float = 0f){
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
                scale = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
}