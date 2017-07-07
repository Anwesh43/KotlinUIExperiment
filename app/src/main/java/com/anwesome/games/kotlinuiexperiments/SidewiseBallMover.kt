package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 07/07/17.
 */
class SidewiseBallMoverView(ctx:Context):View(ctx) {
    var renderer:Renderer = Renderer()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
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
        constructor(w:Int,h:Int,v:SidewiseBallMoverView) {
            this.v = v
            this.w = w
            this.h = h
        }
        fun render(canvas:Canvas,paint:Paint) {

        }
        fun handleTap(x:Float,y:Float) {
        }
    }
}