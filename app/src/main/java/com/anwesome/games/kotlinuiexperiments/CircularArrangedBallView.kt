package com.anwesome.games.kotlinuiexperiments

import android.content.*
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 19/10/17.
 */
class CircularArrangedBallView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean{
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class CircularArrangedBall(var x:Float,var y:Float,var r:Float) {
        var state = CircularArrangedBallState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/10
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,true,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CircularArrangedBallState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 0f
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
    data class CircularArrangedBallContainer(var w:Float,var h:Float,var n:Int) {
        var balls:ConcurrentLinkedQueue<CircularArrangedBall> = ConcurrentLinkedQueue()
        var updatingBalls:ConcurrentLinkedQueue<CircularArrangedBall> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val deg = 360/n
                var currDeg = deg
                val r = Math.min(w,h)/5
                val ballR = Math.min(w,h)/20
                for (i in 1..n) {
                    balls.add(CircularArrangedBall(r*Math.cos(currDeg*Math.PI/180).toInt(),r*Math.sin(currDeg*Math.PI/180).toInt(),ballR))
                    currDeg += deg
                }
            }
        }
        fun update(stopCb:()->Unit) {
            updatingBalls.forEach { ball ->
                ball.update()
                if(ball.stopped()) {
                    updatingBalls.remove(ball)
                    if(updatingBalls.size == 0) {
                        stopCb()
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            balls.forEach { ball ->
                ball.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float,startCb:()->Unit) {
            balls.forEach{ ball ->
                if(ball.handleTap(x-w/2,y-h/2)) {
                    updatingBalls.add(ball)
                    if(updatingBalls.size == 0) {
                        startCb()
                    }
                }
            }
        }
    }
    class CircularArrangedBallAnimator(var container:CircularArrangedBallContainer,var view:CircularArrangedBallView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update({
                    animated = false
                })
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            container.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
    }
}
