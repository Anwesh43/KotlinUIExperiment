package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 14/07/17.
 */
class ColorCollapserView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class CCVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:ColorCollapserView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class DrawingController(w:Float,h:Float,var v:ColorCollapserView) {
        init {

        }
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            if(animated) {
                try {
                    Thread.sleep(50)
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