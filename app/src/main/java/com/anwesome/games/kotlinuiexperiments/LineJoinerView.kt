package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.view.*
import android.graphics.*

/**
 * Created by anweshmishra on 28/10/17.
 */

class LineJoinerView(ctx:Context):View(ctx) {
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
    data class Joint(var i:Float,var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            paint.strokeWidth = size/60
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/15,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,size/15,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(-60f)
            paint.strokeWidth = size/80
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
    data class JointState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = (prevScale+1)%2
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}