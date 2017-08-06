package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 06/08/17.
 */

class GreenToRedButtonView(ctx:Context):View(ctx) {
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
    data class GreenToRedButton(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45*scale)
            paint.color = Color.rgb((255*scale).toInt(),(255*(1-scale)).toInt(),0)
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            paint.color = Color.BLACK
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(i*90.0f)
                canvas.drawLine(0.0f,-size/3,0.0f,size/3,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
}