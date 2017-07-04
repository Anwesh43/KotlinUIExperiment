package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 04/07/17.
 */
class TapAndFillView(ctx:Context):View(ctx) {
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
    data class GrowindBall(var x:Float,var y:Float) {
        var scale:Float = 0.0f
        var mode:Int = 0
        fun draw(canvas:Canvas,paint:Paint,r:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(scale,scale)
            paint.color = Color.YELLOW
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun stopped(h:Float):Boolean = y>=h
        fun update() {
            if(scale >= 1) {
                y+=20.0f
            }
            else {
                scale += 0.2f
                if(scale > 1) {
                    scale = 1.0f
                }
            }
        }
    }
}