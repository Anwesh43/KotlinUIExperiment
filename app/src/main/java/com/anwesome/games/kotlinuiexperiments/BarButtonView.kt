package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 03/08/17.
 */

class BarButtonView(ctx:Context,var n:Int = 5):View(ctx) {
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
    data class BarButton(var x:Float,var y:Float,var r:Float,var h:Float,var scale:Float = 0.0f,var dir:Int = 0) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = r/40
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            canvas.drawLine(0.0f,-h,0.0f,0.0f,paint)
            canvas.restore()
        }
        fun update() {
            scale += dir*0.2f
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r && dir == 0
    }
    class BBVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:BarButtonView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class BBVRenderingController(var barButtons:ConcurrentLinkedQueue<BarButton>,var v:BarButtonView,var tappedButtons:ConcurrentLinkedQueue<BarButton> = ConcurrentLinkedQueue()) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            barButtons.forEach { barButton ->
                barButton.draw(canvas,paint)
            }
        }
        fun update() {
            if(animated) {
                tappedButtons.forEach { tappedButton ->
                    tappedButton.update()
                    if(tappedButton.stopped()) {
                        animated = false
                    }
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
             barButtons.forEach { barButton ->
                if(barButton.handleTap(x,y)) {
                    tappedButtons.add(barButton)
                    if(tappedButtons.size == 1) {
                        animated = true
                        v.postInvalidate()
                    }
                }
             }
        }
    }
}