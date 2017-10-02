package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.view.View
import android.graphics.*
import android.view.MotionEvent

/**
 * Created by anweshmishra on 02/10/17.
 */
class PieColorBarView(ctx:Context):View(ctx) {
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