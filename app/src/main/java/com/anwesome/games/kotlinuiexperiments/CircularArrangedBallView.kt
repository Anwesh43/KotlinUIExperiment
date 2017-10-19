package com.anwesome.games.kotlinuiexperiments

import android.content.*
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 19/10/17.
 */
class CircularArrangedBallView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean{
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
}
