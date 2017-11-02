package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 02/11/17.
 */
import android.graphics.*
import android.content.*
import android.view.*
class HorizontalBarListView(ctx:Context,var n:Int = 10):View(ctx) {
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
    data class HorizontalBar(var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = Math.min(w,h)/40
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45f)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-w/3,0f,w/3,paint)
                canvas.restore()
            }
            canvas.drawRect(RectF(-w/2,w/2,w/2,w/2+h),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-w/2 && x<=this.x+w/2 && y>=this.y-w/2 && y<=this.y+w/2
    }
}