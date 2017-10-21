package com.anwesome.games.kotlinuiexperiments

import android.content.*
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 22/10/17.
 */
class CorrespondingButtonPieView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
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