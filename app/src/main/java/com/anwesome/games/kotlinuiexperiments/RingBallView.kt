package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 03/11/17.
 */
import android.content.*
import android.view.*
import android.graphics.*
class RingBallView(ctx:Context):View(ctx) {
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
