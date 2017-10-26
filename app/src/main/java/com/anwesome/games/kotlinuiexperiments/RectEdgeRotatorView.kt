package com.anwesome.games.kotlinuiexperiments

import android.graphics.*
import android.view.*
import android.content.*
/**
 * Created by anweshmishra on 26/10/17.
 */
class RectEdgeRotatorView(ctx:Context):View(ctx) {
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