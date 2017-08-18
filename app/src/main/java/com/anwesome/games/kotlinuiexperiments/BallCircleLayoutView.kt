package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 18/08/17.
 */
class BallCircleLayoutView(ctx:Context,var n:Int=8):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = BCLRenderer()
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
    data class BCLBall(var cx:Float,var cy:Float,var deg:Float,var r:Float,var size:Float,var r1:Float) {
        var x = 0.0f
        var y = 0.0f
        var state = BCLState()
        init {
            x = cx+(r*Math.cos(deg*Math.PI/180).toFloat())
            y = cy+(r*Math.sin(deg*Math.PI/180).toFloat())
        }
        fun draw(canvas:Canvas,paint: Paint) {
            paint.color = Color.RED
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean {
            val condition = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r && state.stopped()
            if(condition) {
                state.startMoving()
            }
            return condition
        }
        fun update() {
            x = cx+((r+(r1-r)*state.scale)*Math.cos(deg*Math.PI/180).toFloat())
            y = cy+((r+(r1-r)*state.scale)*Math.sin(deg*Math.PI/180).toFloat())
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class BCLState(var scale:Float = 0.0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun startMoving() {
            dir = 1
        }
    }
    class BCLController(var w:Float,var h:Float,var v:BallCircleLayoutView) {
        var balls:ConcurrentLinkedQueue<BCLBall> = ConcurrentLinkedQueue()
        var tappedBalls:ConcurrentLinkedQueue<BCLBall> = ConcurrentLinkedQueue()
        var animated = false
        init {
            var size = w/20
            var r = w/5
            var r1 = (w/2-w/5)
            var n = Math.max(6,v.n)
            var deg = (360/n).toFloat()
            for(i in 0..v.n-1) {
                var ball = BCLBall(w/2,h/2,deg*i,r,size,r1)
                balls.add(ball)
            }
        }
        fun update() {
            if(animated) {
                tappedBalls.forEach { ball ->
                    ball.update()
                    if(ball.stopped()) {
                        tappedBalls.remove(ball)
                        if(tappedBalls.size == 0) {
                            animated = false
                        }
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
        fun draw(canvas:Canvas,paint:Paint){
            balls.forEach { ball ->
                ball.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float) {
            balls.forEach { ball ->
                if(ball.handleTap(x,y)) {
                    tappedBalls.add(ball)
                    if(!animated) {
                        animated = true
                        v.postInvalidate()
                    }
                }
            }
        }
    }
    class BCLRenderer {
        var time = 0
        var controller:BCLController?=null
        fun render(canvas:Canvas,paint:Paint,v:BallCircleLayoutView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                controller = BCLController(w,h,v)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity: Activity) {
            var view = BallCircleLayoutView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}