package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 18/10/17.
 */

class LinkedBallButtonView(ctx:Context):View(ctx) {
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
    data class BallButton(var x:Float,var y:Float,var r:Float,var size:Float) {
        var state:BallButtonState = BallButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = r/10
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,true,paint)
            canvas.drawLine(r,0f,r+size*state.scale,0f,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y -r && y<=this.y+r
    }
    data class BallButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1 ){
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir==0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
    data class LinkedBallButton(var w:Float,var h:Float) {
        var ballButtons:ConcurrentLinkedQueue<BallButton> = ConcurrentLinkedQueue()
        var updatingBalls:LinkedList<BallButton> = LinkedList()
        init {
            val n = 6
            if(w>h) {
                val r = h / 4
                val size = (w - n * r) / n
                var x = r
                for (i in 1..n) {
                    ballButtons.add(BallButton(x,h/2,r,size))
                    x += 2*r+size
                }
            }
        }
        fun update(stopcb:()->Unit) {
            updatingBalls.forEach { ball ->
                ball.update()
                if(ball.stopped()) {
                    updatingBalls.remove(ball)
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            ballButtons.forEach{ ballButton ->
                ballButton.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            ballButtons.forEach { ballButton ->
                if(ballButton.handleTap(x,y)) {
                    updatingBalls.add(ballButton)
                    startcb()
                }
            }
        }
    }
}