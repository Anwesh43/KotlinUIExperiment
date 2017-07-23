package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 23/07/17.
 */
class HorizontalCollapButtonView(ctx:Context):View(ctx) {
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
    class HCBRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:HorizontalCollapButtonView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
                paint.strokeWidth = w/60.0f
                paint.strokeCap = Paint.Cap.ROUND
            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class HCBDrawingController(var v:HorizontalCollapButtonView) {
        var animated = false
        var stateContainer = HCBStateContainer()
        fun draw(canvas:Canvas,paint:Paint) {

        }
        fun update() {
            if(animated) {
                stateContainer.update()
                if(stateContainer.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    v.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                stateContainer.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class HCBStateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += 0.2f*dir
            if(scale > 1 || scale < 0) {
                dir = 0
                scale = if(scale>1) {
                    1.0f
                }
                else {
                    0.0f
                }
            }
        }
        fun stopped():Boolean = dir == 0
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
    }
    data class HCBCollapButton(var x:Float,var y:Float,var r:Float) {
        fun handleTap(x:Float,y:Float):Boolean {
            var dist = (x-this.x)*(x-this.x)-(y-this.y)*(y-this.y)
            return dist < r*r
        }
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#757575")
            canvas.drawCircle(x,y,r,paint)
            paint.color = Color.parseColor("#212121")
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90.0f*i+45.0f*scale)
                canvas.drawLine(0.0f,-3*r/4,0.0f,3*r/4,paint)
                canvas.restore()
            }
        }
    }
}