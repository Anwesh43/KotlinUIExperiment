package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 08/07/17.
 */
class TouchMeterView(ctx:Context):View(ctx){
    val  paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    class TMVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
}