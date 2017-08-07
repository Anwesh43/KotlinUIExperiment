package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 07/08/17.
 */
class TickLineButtonView(ctx:Context):View(ctx) {
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class TickLineButton(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y-canvas.height/2*scale)
            canvas.rotate(360.0f*scale)
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            canvas.drawLine(0.0f,0.0f,0.0f,-size/3,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
    class TickState {
        var scale = 0.0f
        var deg = 0
        fun update() {
            deg+= 18
            scale = Math.abs(Math.sin(deg*Math.PI/180).toFloat())
            if(deg > 180) {
                deg = 0
            }
        }
        fun stopped():Boolean = deg == 0
    }
    class TickRenderController(var button:TickLineButton,var v:TickLineButtonView,var state:TickState = TickState()) {
        var animated = false
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun startUpdating(x:Float,y:Float) {
            if(!animated && button.handleTap(x,y)) {
                animated = true

                v.postInvalidate()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            button.draw(canvas,paint,state.scale)
        }
    }
    class TickRenderer {
        var time = 0
        var controller:TickRenderController?=null
        fun render(canvas:Canvas,paint:Paint,v:TickLineButtonView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                controller = TickRenderController(TickLineButton(w/2,h-w/3,w/3),v)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun startUpdating(x:Float,y:Float) {
            controller?.startUpdating(x,y)
        }
    }
}