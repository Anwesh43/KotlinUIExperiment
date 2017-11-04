package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 04/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class OnCircleButtonView(ctx:Context):View(ctx) {
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
    data class OnCircleButton(var i:Int,var deg:Float,var size:Float,var cx:Float,var cy:Float,var x:Float = cx.xInCircle(size,deg),var y:Float=cy.yInCircle(size,deg)) {
        var state = OnCircleButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(cx,cy)
            canvas.rotate(deg*i)
            canvas.drawCircle(size,0f,size/10,paint)
            canvas.strokeArc(0f,0f,size,deg,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
    }
    data class OnCircleButtonState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = (prevScale+1)%2
                dir = 0f
                prevScale = scale
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
}
fun Canvas.strokeArc(x:Float,y:Float,r:Float,deg:Float,paint:Paint) {
    paint.style = Paint.Style.STROKE
    var d = 0f
    val n = 20
    val gap = deg/n
    val path = Path()
    for(i in 1..n) {
        val rx = x+r*Math.cos(d*Math.PI/180).toFloat()
        val ry = x+r*Math.cos(d*Math.PI/180).toFloat()
        if(i == 1) {
            path.moveTo(rx,ry)
        }
        else {
            path.lineTo(rx,ry)
        }
        d += gap
    }
    this.drawPath(path,paint)
}