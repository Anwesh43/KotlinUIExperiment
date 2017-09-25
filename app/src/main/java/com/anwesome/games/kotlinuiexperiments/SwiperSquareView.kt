package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 25/09/17.
 */
class SwiperSquareView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
    data class SwiperSquare(var y:Float,var w:Float,var x:Float = w/2,var isSelected:Boolean = false) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#FFEB3B")
            if(isSelected) {
                paint.color = Color.parseColor("#009688")
            }
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(-w/10,-w/10,w/10,w/10,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-w/10 && x<=this.x+w/10 && y>=this.y-w/10 && y<=this.y+w/10
    }
}