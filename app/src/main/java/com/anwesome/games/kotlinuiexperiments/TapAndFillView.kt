package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 04/07/17.
 */
class TapAndFillView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class GrowindBall(var x:Float,var y:Float) {
        var scale:Float = 0.0f
        var mode:Int = 0
        fun draw(canvas:Canvas,paint:Paint,r:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(scale,scale)
            paint.color = Color.YELLOW
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun stopped(h:Float):Boolean = y>=h
        fun update() {
            if(scale >= 1) {
                y+=20.0f
            }
            else {
                scale += 0.2f
                if(scale > 1) {
                    scale = 1.0f
                }
            }
        }
    }
    class AnimationHandler {
        var w:Float = 0.0f
        var h:Float = 0.0f
        var animated = false
        var v:TapAndFillView?=null
        var balls:ConcurrentLinkedQueue<GrowindBall> = ConcurrentLinkedQueue()
        constructor(w:Float,h:Float,v:TapAndFillView) {
            this.w = w
            this.h = h
            this.v = v
        }
        fun render(canvas:Canvas,paint:Paint) {
            if(animated) {
                balls.forEach { ball ->
                    ball.draw(canvas,paint,Math.min(w,h)/30)
                    ball.update()
                    if(ball.stopped(h)) {
                        balls.remove(ball)
                        if(balls.size == 0) {
                            animated = false
                        }
                    }
                }
                try {
                    Thread.sleep(50)
                    v?.invalidate()
                }
                catch (ex:Exception) {

                }
            }

        }
        fun handleTap(x:Float,y:Float) {
            balls.add(GrowindBall(x,y))
            if(balls.size == 1) {
                animated = true
                this.v?.postInvalidate()
            }
        }
    }
}