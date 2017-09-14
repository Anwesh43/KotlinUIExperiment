package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/09/17.
 */
class GridLineSquareView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class GridLineSquare(var x:Float,var y:Float,var w:Float,var h:Float,var n:Int,var state:GridLineState = GridLineState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            paint.strokeWidth = w/30
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i*state.scale)
                var x_gap = w/(n+1)
                var cx = x_gap
                for(j in 1..n) {
                    canvas.drawLine(cx-x,-h/2*state.scale,cx-x,h/2*state.scale,paint)
                    cx += x_gap
                }
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class GridLineState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 4.5f
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
}