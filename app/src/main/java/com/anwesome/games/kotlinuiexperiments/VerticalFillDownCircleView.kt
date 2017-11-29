package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 29/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class VerticalFillDownCircleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class VerticalFillDownCircle(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            val path = Path()
            path.addRect(RectF(-r,-r,r,-r+2*r*scale),Path.Direction.CW)
            canvas.clipPath(path)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
    }
}