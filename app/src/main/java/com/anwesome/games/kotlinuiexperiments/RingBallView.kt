package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 03/11/17.
 */
import android.content.*
import android.view.*
import android.graphics.*
class RingBallView(ctx:Context):View(ctx) {
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class CenterCornerBall(var x:Float,var y:Float,var r:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(90f)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,-size,r,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating(deg:Float) {

        }
        fun stopped():Boolean = false
    }
}
