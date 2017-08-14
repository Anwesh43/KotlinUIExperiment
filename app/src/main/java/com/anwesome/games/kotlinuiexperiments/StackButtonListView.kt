package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/08/17.
 */
class StackButton(ctx:Context,var color:Int,var text:String):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class StackButtonShape(var x:Float,var y:Float,var w:Float,var h:Float,var color:Int,var text:String) {
        fun draw(canvas: Canvas,paint:Paint) {
            paint.textSize = h/3
            paint.color = color
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(0.0f,0.0f,w,h),paint)
            paint.color = Color.WHITE
            canvas.drawText(text,-paint.measureText(text)/2,0.0f,paint)
            canvas.restore()
        }
    }
    data class CloseButton(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = size/10
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.WHITE
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x, y)
                canvas.rotate(i*90.0f+45.0f)
                canvas.drawLine(0.0f,-size/2,0.0f,size/2,paint)
                canvas.restore()
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
}