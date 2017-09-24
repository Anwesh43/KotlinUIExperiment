package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 24/09/17.
 */
class DoubleLineArcView(ctx:Context):View(ctx) {
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
    data class CircleAlongLine(var i:Int,var w:Float,var h:Float,var oy:Float = 0.95f*h/20,var y:Float = oy,var r:Float = h/20,var cr:Float = h/5,var x:Float = w/20+0.9f*w*i) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.drawArc(RectF(-cr,-cr,cr,cr),i*180f,180f,true,paint)
            canvas.restore()
            canvas.save()
            canvas.drawLine(x,y,x,oy,paint)
            canvas.drawCircle(x,y,r,paint)
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