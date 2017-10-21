package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.graphics.*
import android.content.Context
/**
 * Created by anweshmishra on 21/10/17.
 */
class FourCornerBallView(ctx:Context):View(ctx) {
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