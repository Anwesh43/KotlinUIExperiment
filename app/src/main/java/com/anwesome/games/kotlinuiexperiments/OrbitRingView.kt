package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 13/07/17.
 */
class OrbitRingView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class ORVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class DrawingController(w:Float,h:Float) {
        var scale = 0.0f
        var dir = 0
        fun draw(canvas:Canvas,paint:Paint) {

        }
        fun handleTap(x:Float,y:Float):Boolean {
            return true
        }
    }
    data class OrbitRing(var x:Float,var y:Float,var size:Float) {
        var scale = 0.0f
        var dir = 0
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(360*scale)
            paint.style = Paint.Style.STROKE
            paint.color = Color.GRAY
            paint.strokeWidth = size/60
            canvas.drawCircle(size/2,size/2,size/2,paint)
            paint.color = Color.BLUE
            canvas.drawArc(RectF(-size/2,size/2,size/2,size/2),-90.0f,360*scale,false,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0.0f,-size/2,size/15,paint)
            canvas.restore()
        }
        fun update() {
            scale += dir * 0.2f
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun stopped():Boolean {
            return dir == 0
        }
        fun startUpdate() {
            if(scale <= 0.0f) {
                dir = 1
            }
            if(scale >= 1.0f) {
                dir = -1
            }
        }
    }
}
