package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
/**
 * Created by anweshmishra on 05/07/17.
 */
class ColorPageView(ctx:Context):View(ctx) {
    var colors:ArrayList<Int> = ArrayList()
    fun addColor(color:Int) {
        colors.add(color)
    }
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                
            }
        }
        return true
    }
}