package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/07/17.
 */
class DotAndLineView(ctx:Context,var n:Int):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer = DALRenderer()
    override fun onDraw(canvas: Canvas) {
        renderer.render(canvas,paint,n,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class DotLine(var r:Float,var n:Int) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            if(n > 0) {
                var deg = 360.0/n
                for (i in 0..n) {
                    canvas.save()
                    canvas.rotate(deg.toFloat()*i)
                    paint.style = Paint.Style.STROKE
                    canvas.drawCircle(0.0f,0.0f,r/10,paint)
                    paint.style = Paint.Style.FILL
                    canvas.drawArc(RectF(-r/10,-r/10,r/10,r/10),0.0f,360*scale,true,paint)
                    var xFact = (r*Math.cos(deg/2).toFloat())*scale
                    var yFact = r*Math.sin(deg/2).toFloat()
                    canvas.drawLine(-2*xFact,yFact,2*xFact,yFact,paint)
                    canvas.restore()
                }
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=-r/10 && x<=r/10 && y>=-r/10 && y<=r/10
    }
    class DALRenderer {
        var time = 0
        var w = 0
        var h = 0
        var drawingController:DALDrawingController?=null
        fun render(canvas:Canvas,paint:Paint,n:Int,v:DotAndLineView) {
            if(time == 0) {
                w = canvas.width
                h = canvas.height
                drawingController = DALDrawingController(DotLine(canvas.width.toFloat()/4,Math.max(n,3)),v)
            }
            drawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.handleTap(x-w.toFloat()/2,y-h.toFloat()/2)
        }
    }
    class DALDrawingController(var dotAndLine:DotLine,var v:DotAndLineView) {
        var animated:Boolean = false
        var stateHandler = StateHandler()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(canvas.width.toFloat()/2,canvas.height.toFloat()/2)
            dotAndLine.draw(canvas,paint,stateHandler.scale)
            canvas.restore()
            if(animated) {
                stateHandler.update()
                if(stateHandler.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (e:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && dotAndLine.handleTap(x,y)) {
                stateHandler.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class StateHandler {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += dir*0.2f
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f->-1
                else -> dir
            }
        }
        fun stopped():Boolean = dir == 0
    }
}
