package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 29/10/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class FanDotView(ctx:Context):View(ctx) {
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
    data class FanDot(var x:Float,var y:Float,var r:Float,var size:Float,var prevDeg:Float  = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x, y)
            canvas.strokeAndScaleFillCircle(0f, 0f, r,1f, paint)
            canvas.drawInATriangle {
                canvas.strokeAndScaleFillCircle(0f,-size,r,1f,paint)
            }
            canvas.drawInATriangle {
                canvas.drawLine(0f,0f,0f,-size,paint)
            }
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}
fun Canvas.drawInATriangle(drawCb:()->Unit) {
    for(i in 0..2) {
        this.save()
        this.rotate(120f*i)
        drawCb()
        this.restore()
    }
}
fun Canvas.strokeAndScaleFillCircle(x:Float,y:Float,r:Float,scale:Float,paint:Paint) {
    paint.style = Paint.Style.STROKE
    this.drawCircle(x,y,r,paint)
    paint.style = Paint.Style.FILL
    this.drawCircle(x,y,r*scale,paint)
}