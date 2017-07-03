package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 03/07/17.
 */
class TappingBallView(ctx:Context):View(ctx) {
    var viewRenderer:ViewRenderer = ViewRenderer()
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        viewRenderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewRenderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class Ball(var x:Float,var y:Float,var r:Float) {
        var deg:Float = 0.0f
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            paint.color = Color.parseColor("#e53935")
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,180.0f,true,paint)
            paint.color = Color.parseColor("#F5F5F5")
            canvas.drawArc(RectF(-r,-r,r,r),180.0f,180.0f,true,paint)
            canvas.restore()
        }
        fun update() {
            deg += 20.0f
            y += 20.0f
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y <= this.y +r
    }
    data class FrameController(var w:Float,var h:Float) {
        val balls:ConcurrentLinkedQueue<Ball> = ConcurrentLinkedQueue()
        fun render(canvas:Canvas,paint:Paint) {
            balls.forEach { ball ->
                ball.draw(canvas,paint)
                ball.update()
            }

        }
        fun createBall() {
            var random = Random()
            var r = random.nextInt((w.toInt()/20)).toFloat()
            balls.add(Ball(random.nextInt(w.toInt()).toFloat(),-r,r))
        }
        fun handleTap(x:Float,y:Float) {
            balls.forEach { ball->
                if(ball.handleTap(x,y)) {
                    balls.remove(ball)
                }
            }
        }
    }
    class ViewRenderer {
        var time = 0
        var frameController:FrameController?=null
        fun render(canvas:Canvas,paint:Paint,v:View) {
            if(time == 0) {
                frameController = FrameController(canvas.width.toFloat(),canvas.height.toFloat())
            }
            frameController?.render(canvas,paint)
            time++
            if(time % 20 == 0) {
                frameController?.createBall()
            }
            try {
                Thread.sleep(50)
                v.invalidate()
            }
            catch (ex:Exception) {

            }
        }
        fun handleTap(x:Float,y:Float) {
            frameController?.handleTap(x,y)
        }
    }
}