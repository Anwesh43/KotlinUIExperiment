package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 18/08/17.
 */
class BallCircleLayoutView(ctx:Context):View(ctx) {
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
    data class BCLBall(var cx:Float,var cy:Float,var deg:Float,var r:Float,var size:Float,var r1:Float) {
        var x = 0.0f
        var y = 0.0f
        var state = BCLState()
        init {
            x = cx+(r*Math.cos(deg*Math.PI/180).toFloat())
            y = cy+(r*Math.sin(deg*Math.PI/180).toFloat())
        }
        fun draw(canvas:Canvas,paint: Paint) {
            paint.color = Color.RED
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean {
            val condition = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r && state.stopped()
            if(condition) {
                state.startMoving()
            }
            return condition
        }
        fun update() {
            x = cx+((r+(r1-r)*state.scale)*Math.cos(deg*Math.PI/180).toFloat())
            y = cy+((r+(r1-r)*state.scale)*Math.sin(deg*Math.PI/180).toFloat())
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class BCLState(var scale:Float = 0.0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun startMoving() {
            dir = 1
        }
    }
}