package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 16/09/17.
 */
class  TriangleArrowButtonView(ctx:Context):View(ctx) {
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
    data class TriangleArrowButton(var x:Float,var y:Float,var l:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            canvas.save()
            canvas.translate(x,y)
            for(i in 1..4) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.translate(0f,l)
                var path = Path()
                path.moveTo(-r/2,r/2)
                path.lineTo(r/2,r/2)
                path.lineTo(0f,-r/2)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}