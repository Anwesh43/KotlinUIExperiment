package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 06/12/17.
 */
import android.graphics.*
import android.content.*
import android.view.*

class CircleFourLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}