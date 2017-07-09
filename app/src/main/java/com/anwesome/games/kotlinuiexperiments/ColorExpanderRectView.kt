package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 09/07/17.
 */
class ColorExpanderRectView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    class CERVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    data class ColorExpanderRect(var x:Float,var y:Float,var size:Float) {
        var scale = 0.0f
        var dir = 0
        fun draw(canvas:Canvas,paint:Paint) {

        }
        fun handleTap(x:Float,y:Float):Boolean  {
            var condition = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10 && dir == 0
            if(condition) {
                dir = when(scale) {
                    0.0f -> 1
                    else -> -1
                }
            }
            return condition
        }
        fun update() {
            scale += dir*0.1f
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
        }
    }
}