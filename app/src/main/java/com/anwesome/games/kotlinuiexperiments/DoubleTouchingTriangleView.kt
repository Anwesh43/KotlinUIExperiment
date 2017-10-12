package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 12/10/17.
 */
class DoubleTouchingTriangleView(ctx:Context):View(ctx) {
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
    data class DoubleTouchingTriangle(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f-2*i,1f)
                canvas.save()
                canvas.drawRotatedHorizontalTriangle(size/2,0f,180f,size,paint)
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
fun Path.addHorizontalTriangle(size:Float) {
    this.moveTo(-size/2,0f)
    this.lineTo(size/2,-size/2)
    this.lineTo(size/2,size/2)
}
fun Canvas.drawRotatedHorizontalTriangle(x:Float,y:Float,deg:Float,size:Float,paint:Paint) {
    this.save()
    this.rotate(deg)
    val path = Path()
    path.addHorizontalTriangle(size)
    this.drawPath(path,paint)
    this.restore()
}