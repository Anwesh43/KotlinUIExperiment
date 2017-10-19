package com.anwesome.games.kotlinuiexperiments

import android.content.*
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 19/10/17.
 */
class CircularArrangedBallView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean{
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class CircularArrangedBall(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/10
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f,true,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
}
