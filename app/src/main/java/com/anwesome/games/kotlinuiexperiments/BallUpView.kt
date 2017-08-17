package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 17/08/17.
 */
class BallUpView(ctx:Context,var n:Int = 7):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer = BallUpRenderer()
    var listener:OnBallUpListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class BallUp(var index:Int,var x:Float,var y:Float,var r:Float,var h:Float) {
        var origY = 0.0f
        init {
            origY = y
        }

        fun draw(canvas: Canvas, paint: Paint) {
            canvas.save()
            canvas.translate(x, y)
            paint.color = Color.parseColor("#ef5350")
            canvas.drawCircle(0.0f, 0.0f, r, paint)
            canvas.restore()
        }

        fun update(scale: Float) {
            y = origY - h * scale
        }
        fun handleTap(x: Float, y: Float): Boolean = x >= this.x - r && x <= this.x + r && y >= this.y - r && y <= this.y + r
    }
    class BallUpRenderer {
        var time = 0
        var animator:BallUpAnimator?=null
        fun render(canvas:Canvas,paint:Paint,v:BallUpView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                animator = BallUpAnimator(w,h,v)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    class BallUpAnimator(var w:Float,var h:Float,var v:BallUpView,var state:BallUpState = BallUpState()) {
        var balls:ConcurrentLinkedQueue<BallUp> = ConcurrentLinkedQueue()
        var tappedBall:BallUp?=null
        var animated = false
        init {
            var gap = w/(2*v.n+1)
            for(i in 0..v.n) {
                var ball = BallUp(i,0.0f,h/2,2*(gap/3),h/2)
                balls.add(ball)
            }
            adjustBalls()
        }
        private fun adjustBalls() {
            var gap = w/(2*v.n+1)
            var x = 3*gap/2
            balls.forEach { ball ->
                ball.x = x
                x += 2*gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            balls.forEach { ball ->
                ball.draw(canvas,paint)
            }
        }
        fun update() {
            if(animated) {
                state.update()
                tappedBall?.update(state.scale)
                if(state.stopped()) {
                    animated = false
                    v?.listener?.onBallUp(tappedBall?.index?:0)
                    balls.remove(tappedBall)
                    v.n--
                    if(v.n != 0) {
                        adjustBalls()
                    }
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && v.n>0) {
                balls.forEach { ball ->
                    if(ball.handleTap(x,y)) {
                        tappedBall = ball
                        animated = true
                        v.postInvalidate()
                    }
                }
            }
        }
    }
    class BallUpState(var scale:Float = 0.0f){
        fun update() {
            scale += 0.1f
            if(scale > 1) {
                scale = 0.0f
            }
        }
        fun stopped():Boolean = scale == 0.0f
    }
    companion object {
        var view:BallUpView?=null
        fun create(activity:Activity) {
            view = BallUpView(activity)
            activity.setContentView(view)
        }
        fun addListener(listener:OnBallUpListener) {
            view?.listener = listener
        }
    }
    interface OnBallUpListener {
        fun onBallUp(index:Int)
    }
}