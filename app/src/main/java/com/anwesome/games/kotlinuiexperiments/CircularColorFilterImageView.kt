package com.anwesome.games.kotlinuiexperiments
import android.graphics.*
import android.content.Context
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 19/07/17.
 */
class CircularColorFilterImageView(bitmap:Bitmap,ctx:Context):View(ctx) {
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
    class CCFIVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:CircularColorFilterImageView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap() {

        }
    }
}