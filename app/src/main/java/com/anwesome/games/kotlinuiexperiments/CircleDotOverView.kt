package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 10/12/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class CircleDotOverView(ctx:Context):View(ctx) {
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