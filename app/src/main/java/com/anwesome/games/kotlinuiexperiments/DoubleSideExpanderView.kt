package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 11/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class DoubleSideExpanderView(ctx:Context):View(ctx) {
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
    data class SideExpander(var i:Int,var w:Float,var h:Float,var x:Float=w/2-(w/4+w/10)*(2*i-1),var y:Float=h/2) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,w/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w/10,-w/10,w/10,w/10),0f,360f,true,paint)
            canvas.restore()
            val rx = w/2-w/4+i*(w/4)
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.drawRect(RectF(rx-w/2,-w/8,rx+w/2,w/8),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}