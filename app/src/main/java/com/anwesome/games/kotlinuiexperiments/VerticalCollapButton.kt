package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 21/07/17.
 */
class VerticalCollapButton(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class VCBRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:VerticalCollapButton) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class VCBStateContainer(var scale:Float=0.0f,var dir:Int = 0) {
        fun update() {
            scale += dir*0.2f
            if(scale > 1.0f) {
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
                1.0f -> -1
                else -> dir
            }
        }
        fun stopped():Boolean = dir == 0
    }
    class VCBRenderingController(var v:VerticalCollapButton) {
        var animated = false
        var stateContainer = VCBStateContainer()
        fun animate() {
            if(animated) {
                stateContainer.update()
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                animated = true
                stateContainer.startUpdating()
            }
        }
    }
    data class CollapButton(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.BLACK
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90.0f*i+45.0f*scale)
                canvas.drawLine(0.0f,-2*r/3,0.0f,2*r/3)
                canvas.restore()
            }
            canvas.restore()
        }
    }
    data class VerticalButton(var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawRect(RectF(0,0,w,h*scale),paint)
            canvas.restore()
        }
    }
}