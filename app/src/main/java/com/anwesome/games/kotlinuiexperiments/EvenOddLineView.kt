package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/12/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class EvenOddLineView(ctx:Context):View(ctx) {
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
    data class EvenOddLine(var i:Int,var x:Float,var py:Float,var h:Float,var y:Float=py) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            val j = i%2
            y = - h*(j+(1-2*j)*scale)
            canvas.save()
            canvas.translate(x,py)
            canvas.drawLine(0f,0f,0f,y,paint)
            canvas.restore()
        }
    }
    data class EvenOddLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1){
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
}