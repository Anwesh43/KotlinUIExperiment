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
        var state = HorizontalBlockMoverState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x+hor_w,y)
            canvas.drawRect(RectF(-w/2,-w/2,w/2,w/2),paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class HorizontalBlockMoverState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                prevScale = scale
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
}