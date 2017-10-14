package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 14/10/17.
 */
class CenterToCornerBallView(ctx:Context):View(ctx) {
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
    data class CornerBall(var i:Float,var w:Float,var h:Float,var x:Float = 0f,var y:Float = 0f,var r:Float = Math.min(w,h)/20) {
        init {
            x = (i%2)*(w-2*r)+r
            y = (i/2)*(h-2*r)+r
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = r/10
            paint.color = Color.parseColor("#FF9800")
            canvas.save()
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(x,y,r,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
}