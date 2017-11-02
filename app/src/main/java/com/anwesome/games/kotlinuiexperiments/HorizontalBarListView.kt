package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 02/11/17.
 */
import android.graphics.*
import android.content.*
import android.view.*
class HorizontalBarListView(ctx:Context,var n:Int = 10):View(ctx) {
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