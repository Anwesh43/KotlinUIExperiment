package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 24/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
class UpDownBallView(ctx:Context):View(ctx) {
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
}