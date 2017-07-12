package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 12/07/17.
 */

class ScaleHorizontalButtonListView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    data class ScaleHorizontalButton(var x:Float,var y:Float,var w:Float,var h:Float) {
        var scale:Float = 0.0f
        var mode:Int = 0
        var waitParam:Int = 0
        fun update() {
            when(mode) {
                1 -> {
                    scale+=0.2f
                    if(scale > 1) {
                        mode = 2
                        scale = 1.0f
                    }
                }
                2 ->  {
                    waitParam++
                    if(waitParam == 5) {
                        waitParam = 0
                        mode = 3
                    }
                }
                3 -> {
                    scale -= 0.2f
                    if(scale < 0) {
                        scale = 0.0f
                        mode = 0
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawRect(RectF(-w/2,-h/2,w/2,h/2),paint)
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x,y)
                canvas.rotate(i*180.0f)
                paint.color = Color.parseColor("#FFEA00")
                canvas.drawRect(RectF(w/2,-h/2,w/4*scale,h/2),paint)
                canvas.restore()
            }
        }
        fun stopped():Boolean = mode == 0
    }
}