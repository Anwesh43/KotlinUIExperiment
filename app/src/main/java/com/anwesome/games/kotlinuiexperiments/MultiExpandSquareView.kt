package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 15/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class MultiExpandSquareView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class MultiExpandSquare(var i:Int,var n:Int,var x:Float,var y:Float,var size:Float,var cx:Float = x,var cy:Float = y) {
        fun draw(canvas:Canvas,paint:Paint) {
            val gap = ((n/2-i)*size/4)
            x = cx - gap
            y = cy - gap
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(-size/2,-size/2,size/2,size/2),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
    data class MutliExpandSquareState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                dir = 0f
                scale = (prevScale+1)%2
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*this.prevScale
        }
        fun stopped():Boolean = dir == 0f
    }
}