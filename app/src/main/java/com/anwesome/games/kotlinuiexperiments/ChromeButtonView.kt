package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 08/09/17.
 */
class ChromeButtonView(ctx:Context):View(ctx) {
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
    data class ChromeButton(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#0288D1")
            canvas.drawCircle(0f,0f,size/5,paint)
            paint.color = Color.argb(100,0,0,0)
            canvas.drawArc(RectF(-size/5,-size/5,size/5,size/5),0f,360f,true,paint)
            val colors = arrayOf("#f44336","#FFD740","#388E3C")
            var deg = 0f
            colors.forEach{ color ->
                paint.color = Color.parseColor(color)
                canvas.drawArc(RectF(-size/3,-size/3,size/3,size/3),deg,120f,true,paint)
                deg += 120
            }
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
        fun handleTap(x:Float,y:Float) = x>=this.x - size/5 && x<=this.x+size/5 && y>=this.y -size/5 && y<=this.y+size/5
    }
}