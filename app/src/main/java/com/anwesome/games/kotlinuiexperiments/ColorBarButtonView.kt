package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 16/10/17.
 */
class ColorBarButtonView(ctx:Context):View(ctx) {
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
    data class ColorScreen(var w:Float,var h:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = color
            canvas.save()
            canvas.translate(0f,h/10)
            canvas.drawRect(RectF(0f,0f,w,9*h/10),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
}