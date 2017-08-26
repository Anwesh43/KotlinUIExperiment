package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 27/08/17.
 */
class MultiCircularButtonView(ctx:Context,var n:Int = 6):View(ctx) {
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
    data class ControlButton(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45*scale)
            paint.color = Color.parseColor("#304FFE")
            canvas.drawCircle(0f,0f,r,paint)
            paint.color = Color.WHITE
            paint.strokeWidth = r/30
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-2*r/3,0f,2*r/3,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CircularButton(var x:Float,var y:Float,var r:Float,var scale:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#304FFE")
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            canvas.scale(scale,scale)
            paint.color = Color.argb(130,255,255,255)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            scale+=0.1f
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class MultiCircularButton(var cx:Float,var cy:Float,var finalR:Float,var r:Float=0f,var n:Int,var gapDeg:Float = 360f/n) {
        var circularButtons:ConcurrentLinkedQueue<CircularButton> = ConcurrentLinkedQueue()
        var tappedButtons:ConcurrentLinkedQueue<CircularButton> = ConcurrentLinkedQueue()
        init {
            var gapDeg = 360f/n
            var r1 = r/5
            for(i in 0..n-1) {
                var deg = i*gapDeg
                var x = cx+(r-r1)*Math.cos(deg*Math.PI/180).toFloat()
                var y = cy+(r-r1)*Math.sin(deg*Math.PI/180).toFloat()
                var circularButton = CircularButton(x,y,r1)
                circularButtons.add(circularButton)
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            circularButtons.forEach{ circularButton ->
                circularButton.draw(canvas,paint)
            }
        }
        fun update(scale:Float) {
            r = finalR*scale
            var r1 = r/5
            var deg = 0f
            circularButtons.forEach { circularButton ->
                circularButton.x = cx+(r-r1)*Math.cos(deg*Math.PI/180).toFloat()
                circularButton.y = cy+(r-r1)*Math.sin(deg*Math.PI/180).toFloat()
                deg+=gapDeg
            }
        }
        fun updateTappedButon() {
            tappedButtons.forEach {

            }
        }
        fun handleTap(x:Float,y:Float):Boolean {
            circularButtons.forEach { cb ->
                if(cb.handleTap(x,y)) {
                    return true
                }
            }
            return false
        }
    }
}