package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 14/08/17.
 */
class SwappableCircleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class SwappableCircle(var x:Float,var y:Float,var r:Float) {
        var traversePath:TraversePath?=null
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#00838F")
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun update() {
            traversePath?.update()
            x = traversePath?.x?:x
            y = traversePath?.y?:y
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class TraversePath(var x:Float,var y:Float,var r:Float) {
        var deg = 0.0f
        var traversePoint = PointF()
        fun update() {
            traversePoint.x = x+r*(Math.cos(deg*Math.PI/180)).toFloat()
            traversePoint.y = y+r*(Math.sin(deg*Math.PI/180)).toFloat()
        }
    }
}