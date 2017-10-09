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
    data class PointedArrowState(var scale:Float = 0f,var dir:Int = 0,var currDir:Int = 1,var j:Int = 0) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1 && currDir > 0) {
                dir = 0
                scale = 0f
                j++
                if(j == 4) {
                    currDir = -1
                    scale = 1f
                }
            }
            else if(scale < 0 && currDir < 0) {
                dir = 0
                scale = 1f
                j--
                if(j == 0) {
                    currDir = 1
                    scale = 0f
                }
            }
        }
        fun startUpdaing() {
            dir = currDir
        }
        fun stopped():Boolean = dir == 0
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