package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 07/10/17.
 */
class FillDownFourTriangleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class FillDownFourTriangle(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                val path = Path()
                path.moveTo(-w/3,-h/3)
                path.lineTo(0f,-h/3+h/3)
                path.lineTo(w/3,h/3)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
    data class FillDownTriangleState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale = Math.sin(deg*Math.PI/180).toFloat()
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
}