package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 10/11/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class FourSideWheelView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class FourSideWheel(var w:Float,var h:Float) {
        var state = FourSideWheelState()
        fun draw(canvas:Canvas,paint:Paint) {
            val r = Math.min(w,h)/3
            val r1 = 0.4f*Math.min(w,h)
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.rotate(state.deg)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,r*state.scale,paint)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawArc(RectF(-r1,-r1,r1,r1),15f,60f,true,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class FourSideWheelState(var dir:Float = 0f,var deg:Float = 0f,var prevDeg:Float = 0f,var scale:Float = 0f) {
        fun update() {
            deg = prevDeg + 90f*0.1f*dir
            scale += Math.sin(2*(deg-prevDeg)*Math.PI/180).toFloat()
            if(prevDeg - deg >= 90) {
                deg = prevDeg +90f
                prevDeg = deg
                dir = 0f
                scale = 0f
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
}