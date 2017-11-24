package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 24/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class UpDownBallView(ctx:Context,var n:Int = 5):View(ctx) {
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
    data class UpDownBall(var x:Float,var y:Float,var r:Float,var h:Float,var py:Float = y) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            y = py + h*scale
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
        }
    }
    data class UpDownBallContainer(var w:Float,var h:Float,var n:Int) {
        var upDownBalls:ConcurrentLinkedQueue<UpDownBall> = ConcurrentLinkedQueue()
        init {
            val gap = w/(2*n+1)
            var x:Float = gap*1.5f - w/2
            for(i in 0..n-1) {
                upDownBalls.add(UpDownBall(x,gap-h/2,gap/2,2*h/5-gap))
                x+= 2*gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w/2,h/2)
                canvas.scale(1f,1f-2*i)
                upDownBalls.forEach { upDownBall ->
                    upDownBall.draw(canvas,paint,1f)
                }
                canvas.restore()
            }
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
    data class UpDownBallState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                prevScale = scale
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}