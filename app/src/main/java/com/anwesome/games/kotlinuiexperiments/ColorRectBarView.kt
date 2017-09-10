package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 10/09/17.
 */
class ColorRectBarView(ctx:Context):View(ctx) {
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
    data class ColorRectBar(var w:Float,var h:Float,var x:Float = w/2,var y:Float = h/2,var r:Float = Math.min(w,h)/10) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#FF5722")
            canvas.save()
            canvas.translate(x,y)
            paint.strokeWidth = r/5
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f,true,paint)
            canvas.restore()
            canvas.save()
            canvas.translate(0f,0.7f*h)
            paint.style = Paint.Style.STROKE
            canvas.drawRoundRect(RectF(0f,0f,w,h/10),r/2,r/2,paint)
            val colors = arrayOf(Color.parseColor("#01579B"),Color.parseColor("#004D40"),Color.parseColor("#D81B60"),Color.parseColor("#00B8D4"),Color.parseColor("#c62828"))
            var rx = -r/2
            paint.style = Paint.Style.FILL
            for(color in colors) {
                canvas.save()
                paint.color = color
                var path = Path()
                path.addRect(RectF(rx,0F,rx+(w+r)/5,h/10),Path.Direction.CW)
                canvas.drawRoundRect(RectF(0f,0f,w,h/10),r/2,r/2,paint)
                canvas.restore()
                rx += (w+r)/5
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x >= this.x - r && x <= this.x+r && y >= this.y - r && y <= this.y + r
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
    data class ColorRectBarState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir * 0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1f - 2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}