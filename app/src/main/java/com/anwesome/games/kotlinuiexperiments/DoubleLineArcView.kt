package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 24/09/17.
 */
class DoubleLineArcView(ctx:Context):View(ctx) {
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
    data class CircleAlongLine(var i:Int,var w:Float,var h:Float,var oy:Float = 0.95f*h/20,var y:Float = oy,var r:Float = h/20,var cr:Float = h/5,var x:Float = w/20+0.9f*w*i) {
        var state:CALState = CALState()
        fun draw(canvas:Canvas,paint:Paint) {
            y = oy - 0.9f*h*state.scale
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.drawArc(RectF(-cr,-cr,cr,cr),i*180f,180f*state.scale,true,paint)
            canvas.restore()
            canvas.save()
            canvas.drawLine(x,y,x,oy,paint)
            canvas.drawCircle(x,y,r,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = false
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CALState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
    class DoubleLineArcAnimator(var circleAlongLines:ConcurrentLinkedQueue<CircleAlongLine>,var view:DoubleLineArcView) {
        var tappedCircleAlongLines = ConcurrentLinkedQueue<CircleAlongLine>()
        var animated = false
        fun update() {
            if(animated) {
                tappedCircleAlongLines.forEach { tappedCircleAlongLine ->
                    tappedCircleAlongLine.update()
                    if(tappedCircleAlongLine.stopped()) {
                        tappedCircleAlongLines.remove(tappedCircleAlongLine)
                        if(tappedCircleAlongLines.size == 0) {
                            animated = false
                        }
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            circleAlongLines.forEach { circleAlongLine ->
                circleAlongLine.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float) {
            circleAlongLines.forEach { circleAlongLine ->
                if(circleAlongLine.handleTap(x,y)) {
                    tappedCircleAlongLines.add(circleAlongLine)
                    if(tappedCircleAlongLines.size == 1) {
                        animated = true
                        view.postInvalidate()
                    }
                }
            }
        }
    }
}