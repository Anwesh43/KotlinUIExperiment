package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class RotatorDotLineView(ctx:Context):View(ctx) {
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
    data class RotatorLine(var x:Float,var y:Float,var deg:Float=0f,var size:Float,var prevDeg:Float = 0f) {
        var state = RotatorLineState()
        fun update() {
            state.startUpdating()
        }
        fun startUpdating(finalDeg:Float) {
            deg = finalDeg - prevDeg
        }
        fun stopped():Boolean {
            val stopCondition = state.stopped()
            if(stopCondition) {
                prevDeg += deg
            }
            return stopCondition
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(prevDeg+deg*state.scale)
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
        }
    }
    data class RotatorLineState(var dir:Float= 0f,var scale:Float = 0f){
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
                scale = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    data class LineDot(var i:Int,var x:Float,var cy:Float,var size:Float,var y:Float = cy+size*(2*i-1),var deg:Float = 90f*(2*i-1)) {
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.strokeWidth = size/25
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,size/10,paint)
            canvas.restore()
        }
    }
}