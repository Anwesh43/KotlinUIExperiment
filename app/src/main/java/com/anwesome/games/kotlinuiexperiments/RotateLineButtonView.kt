package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 23/10/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
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
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            canvas.scale(1f,1f)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(90f*(1-2*i))
            canvas.drawLine(0f,-r,0f,-r-size,paint)
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