package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
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

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class HCBDrawingController(var v:HorizontalCollapButtonView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {

        }
        fun update() {
            if(animated) {
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
                animated = true
                v.postInvalidate()
            }
        }
    }
}