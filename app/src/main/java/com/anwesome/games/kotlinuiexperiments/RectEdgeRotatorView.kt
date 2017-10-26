package com.anwesome.games.kotlinuiexperiments

import android.graphics.*
import android.view.*
import android.content.*
/**
 * Created by anweshmishra on 26/10/17.
 */
class RectEdgeRotatorView(ctx:Context):View(ctx) {
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
    data class RectEdgeRotator(var x:Float,var y:Float,var size:Float){
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            canvas.rotate(-30f)
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(-30f*i)
                canvas.drawCircle(0f,size,size/6,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}