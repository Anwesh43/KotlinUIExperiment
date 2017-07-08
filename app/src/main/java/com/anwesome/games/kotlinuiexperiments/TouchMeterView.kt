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
    var renderer = TMVRenderer()
    val  paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        renderer.handleTap(event.action)
        return true
    }
    class TMVRenderer {
        var time = 0
        var down = false
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(action:Int) {
            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    if(!down) {

                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(down) {

                    }
                }
            }
        }
    }
}