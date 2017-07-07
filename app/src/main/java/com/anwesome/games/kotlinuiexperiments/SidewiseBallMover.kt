package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 07/07/17.
 */
class SidewiseBallMoverView(ctx:Context):View(ctx) {
    var renderer:Renderer = Renderer()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
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
    class Renderer {
        var time = 0
        var v:SidewiseBallMoverView?=null
        var drawingController:DrawingController? = null
        fun render(canvas:Canvas,paint:Paint,v:SidewiseBallMoverView) {
            if(time == 0) {
                drawingController = DrawingController(canvas.width,canvas.height,v)
            }
            drawingController?.render(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.handleTap(x,y)
        }
    }
    class DrawingController {
        var w:Int = 0
        var h:Int = 0
        var animated:Boolean = true
        var v:SidewiseBallMoverView?=null
        var sideWiseBalls:ConcurrentLinkedQueue<SideWiseBall> = ConcurrentLinkedQueue()
        var movingBalls:ConcurrentLinkedQueue<SideWiseBall> = ConcurrentLinkedQueue()
        constructor(w:Int,h:Int,v:SidewiseBallMoverView) {
            this.v = v
            this.w = w
            this.h = h
            this.initBalls()
        }
        fun initBalls() {
            val n = 8
            val r:Float = (h/(2*n+1)).toFloat()
            var y = 3*r
            for(i in 1..n) {
                sideWiseBalls.add(SideWiseBall((w/2).toFloat(),y,r))
                y+=2*r
            }
        }
        fun render(canvas:Canvas,paint:Paint) {
            sideWiseBalls.forEach{ ball ->
                ball.draw(canvas,paint)
            }
            if(animated) {
                movingBalls.forEach {
                    it.draw(canvas,paint)
                }
                try {
                    Thread.sleep(75)
                    v?.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            sideWiseBalls.forEach { ball ->
                if(ball.handleTap(x,y)) {
                    movingBalls.add(ball)
                    if(!animated) {
                        animated = true
                        v?.postInvalidate()
                    }
                }
            }
        }
    }
    data class SideWiseBall(var x:Float,var y:Float,var r:Float) {
        var dir:Int = 0
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean {
            var condition = y>=this.y-r && y<=this.y+r && x>=this.x-r && x<=this.x+r && Math.abs(x-this.x)>=r
            if(condition) {
                dir = ((x-this.x)/(Math.abs(x-this.x))).toInt()
            }
            return condition
        }
        fun stopped():Boolean = dir == 0
        fun update(bounds:Float) {
            x+=dir*(bounds/5)
            if(x>bounds-r) {
                dir = 1
                x = bounds-r
            }
            if(x < r) {
                dir = 1
                x = r
            }
            if(dir == 1 && x<bounds/2) {
                x = bounds/2
                dir = 0
            }
            if(dir == -1 && x>bounds/2) {
                x = bounds/2
                dir = 0
            }
        }
    }
}