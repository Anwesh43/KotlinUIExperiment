package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 21/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
class CompletionIndicatorView(ctx:Context):View(ctx) {
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
    data class CompletionIndicator(var x:Float,var y:Float) {
        fun draw(canvas:Canvas,paint:Paint) {

        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
    }
}