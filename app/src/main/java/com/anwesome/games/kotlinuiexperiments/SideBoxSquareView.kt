package com.anwesome.games.kotlinuiexperiments
import android.graphics.*
import android.content.*
import android.view.*
/**
 * Created by anweshmishra on 08/12/17.
 */
class SideBoxSquareView(ctx:Context,var n:Int = 6):View(ctx) {
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