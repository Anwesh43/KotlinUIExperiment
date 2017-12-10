package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 10/12/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class CircleDotOverView(ctx:Context):View(ctx) {
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
    data class CircleDot(var i:Int,var x:Float,var y:Float,var r:Float,var deg:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            val new_deg = deg*i
            val update_deg = deg
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(new_deg+update_deg)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/7
            canvas.drawArc(RectF(-r,-r,r,r),new_deg,update_deg,false,paint)
        }
        fun update(stopcb:(Int,Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class CircleDotState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
}