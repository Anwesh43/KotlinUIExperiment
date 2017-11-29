package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 29/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class VerticalFillDownCircleView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class VerticalFillDownCircle(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            val path = Path()
            path.addRect(RectF(-r,-r,r,-r+2*r*scale),Path.Direction.CW)
            canvas.clipPath(path)
            paint.color = Color.parseColor("#01579B")
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
    }
    data class VerticalFillController(var w:Float,var h:Float) {
        var circle = VerticalFillDownCircle(w/2,h/2,Math.min(w,h)/3)
        fun draw(canvas:Canvas,paint:Paint) {
            circle.draw(canvas,paint,1f)
        }
        fun update(stopcb:()->Unit) {

        }
        fun startUpdating(starcb:()->Unit) {

        }
    }
}