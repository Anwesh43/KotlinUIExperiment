package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 20/10/17.
 */
class ArrowTipRotatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class ArrowTipRotator(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = size/12
            canvas.save()
            canvas.translate(x,y)
            canvas.drawLine(0f,0f,0f,-size,paint)
            canvas.save()
            canvas.translate(0f,-size)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate((2*i-1)*45f)
                canvas.drawLine(0f,0f,0f,size/4,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
    data class ArrowTipRotatorState(var scale:Float=0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0f) {
                dir = 0f
                scale = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}