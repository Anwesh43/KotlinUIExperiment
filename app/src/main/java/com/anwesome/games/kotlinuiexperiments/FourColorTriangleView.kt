package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 29/08/17.
 */
class FourColorTriangleView(ctx:Context):View(ctx) {
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
    data class FourColorTriangle(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.scale(scale,scale)
                var path = Path()
                path.moveTo(0f,0f)
                path.lineTo(-size/2,-size/2)
                path.lineTo(size/2,-size/2)
                path.lineTo(0f,0f)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
    data class FCTState(var scale:Float = 0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1 || scale < 0) {
                dir = 0
                if(scale > 1) {
                    scale = 1f
                }
                if(scale < 0) {
                    scale = 0f
                }
            }
        }
        fun stopUpdating():Boolean = dir == 0
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
    }
}