package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 20/11/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class JumpingLineJointView(ctx:Context):View(ctx) {
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