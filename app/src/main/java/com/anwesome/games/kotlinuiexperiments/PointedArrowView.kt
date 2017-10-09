package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 10/10/17.
 */
class PointedArrowView(ctx:Context):View(ctx) {
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
    data class PointedArrow(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            val j = Math.floor(3.0).toInt()
            for(i in 0..3) {
                canvas.drawDirectedLine(size/2,i*90f,paint)
            }
            canvas.drawDirectedLine(size/2,j*90f+90f,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
}
fun Canvas.drawDirectedLine(size:Float,deg:Float,paint:Paint) {
    this.save()
    this.rotate(deg)
    this.drawLine(size,-size/2,size,-size/2,paint)
    val path = Path()
    path.addDownTriangle(size,size/2,size/10)
    this.drawPath(path,paint)
    this.restore()
}
fun Path.addDownTriangle(x:Float,y:Float,size:Float) {
    this.moveTo(x-size/2,y-size/2)
    this.lineTo(x+size/2,y-size/2)
    this.lineTo(x,y+size/2)
}