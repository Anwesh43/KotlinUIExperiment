package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
}