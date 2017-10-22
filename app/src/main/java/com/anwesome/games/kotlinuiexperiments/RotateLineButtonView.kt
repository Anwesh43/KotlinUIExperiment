package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 23/10/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class RotateLineButtonView(ctx:Context):View(ctx) {
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
    data class RotateLineButton(var i:Int,var x:Float,var y:Float,var r:Float,var size:Float) {
        var state = RotateLineButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            canvas.scale(state.scale,state.scale)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(90f*(1-2*i)*state.scale)
            canvas.drawLine(0f,-r,0f,-r-size,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
        fun stopped():Boolean = state.stopped()
    }
    data class RotateLineButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale+=dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f

        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
    data class RotateLineButtonContainer(var w:Float,var h:Float) {
        var  rotatingLines:ConcurrentLinkedQueue<RotateLineButton> = ConcurrentLinkedQueue()
        var updatingLines:ConcurrentLinkedQueue<RotateLineButton> = ConcurrentLinkedQueue()
        fun update() {

        }
        fun handleTap(x:Float,y:Float) {

        }
    }
}