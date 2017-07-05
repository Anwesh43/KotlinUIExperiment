package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
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
class Renderer {
    var time:Int = 0
    fun render(canvas:Canvas,colors:List<Int>) {
        if(time == 0) {

        }
        time++
    }
    fun handleTap() {

    }
}
data class ColorPage(var color:Int) {
    var scale:Float = 0.0f
    var dir:Float = 0.0f
    fun draw(canvas:Canvas,paint:Paint,w:Int,h:Int) {
        paint.color = color
        canvas.drawRect(RectF(0.0f,0.0f,w.toFloat(),h*scale),paint)
    }
    fun update() {
        scale+=0.2f*dir
        if(scale < 0) {
            scale = 0.0f
            dir = 0.0f
        }
    }
}
