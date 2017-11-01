package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 01/11/17.
 */
class BarOverView(ctx:Context):View(ctx) {
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
    data class BarOver(var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x+(w/2*(i*2-1)),y)
                paint.color = Color.parseColor("#1565C0")
                canvas.drawRect(RectF(-w/2,-h/2,w/2,h/2),paint)
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