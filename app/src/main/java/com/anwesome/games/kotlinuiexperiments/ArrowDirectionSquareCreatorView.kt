package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*

/**
 * Created by anweshmishra on 13/10/17.
 */
class ArrowDirectionSquareCreatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        if(event.action == MotionEvent.ACTION_DOWN) {

        }
        return true
    }
}