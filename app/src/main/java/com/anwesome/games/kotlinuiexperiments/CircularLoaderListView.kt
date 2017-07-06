package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 06/07/17.
 */
class CircularLoaderListView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event: MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class Renderer {
        var time = 0
        fun render(canvas:Canvas?,paint:Paint) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    data class CircularLoader(var x:Float,var y:Float,var r:Float) {
        var startDeg = 0.0f
        var endDeg = 0.0f
        var dir = 0.0f
        fun draw(canvas:Canvas?,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/40
            canvas?.save()
            canvas?.translate(x,y)
            canvas?.drawArc(RectF(-r,-r,r,r),0.0f,360.0f,false,paint)
            paint.color = Color.WHITE
            canvas?.drawArc(RectF(-r,-r,r,r),startDeg-90,endDeg,false,paint)
            canvas?.restore()
        }
        fun update() {
            if(startDeg < 300) {
                endDeg+=10*dir
            }
            if(endDeg >= 90) {
                startDeg+=10*dir
            }
            if(startDeg >= 360) {
                startDeg = 360.0f
                endDeg = 0.0f
                dir = 0.0f
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x -r && x<=this.x+r && y>=this.y-r && y<=this.y+r 
    }
}