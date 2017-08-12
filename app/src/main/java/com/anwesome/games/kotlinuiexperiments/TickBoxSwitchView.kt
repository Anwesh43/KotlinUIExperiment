package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 12/08/17.
 */
class TickBoxSwitchView(ctx:Context,var n:Int = 5):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
    data class TickBox(var x:Float,var y:Float,var size:Float,var scale:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.WHITE
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            paint.color = Color.parseColor("#1976D2")
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
            paint.color = Color.WHITE
            paint.strokeWidth = size/15
            canvas.save()
            canvas.rotate(-45.0f)
            canvas.drawLine(0.0f,0.0f,0.0f,-size/6,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(60.0f)
            canvas.drawLine(0.0f,0.0f,0.0f,-size/3,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x - size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
}