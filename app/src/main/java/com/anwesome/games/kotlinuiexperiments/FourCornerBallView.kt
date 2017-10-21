package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.graphics.*
import android.content.Context
/**
 * Created by anweshmishra on 21/10/17.
 */
class FourCornerBallView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class FourCornerBall(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..3) {
                canvas.save()
                canvas.translate(w/2,h/2)
                canvas.drawCircle(0f-((w/3)+(2*w/3)*(i%2)),0f-((w/3+(2*w/3)*(i/2))),Math.min(w,h)/10,paint)
                canvas.restore()
            }
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}