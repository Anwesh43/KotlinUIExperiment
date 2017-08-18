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
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
        fun update() {

        }
        fun stopped():Boolean = true
    }
}