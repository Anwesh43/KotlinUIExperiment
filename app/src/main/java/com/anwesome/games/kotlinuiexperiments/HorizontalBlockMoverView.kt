package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 28/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class HorizontalBlockMoverView(ctx:Context,var n:Int = 4):View(ctx) {
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
    data class HorizontalBlockMover(var i:Int,var prevX:Float,var y:Float,var w:Float,var hor_w:Float,var x:Float = prevX - hor_w) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x+hor_w,y)
            canvas.drawRect(RectF(-w/2,-w/2,w/2,w/2),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}