package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 24/07/17.
 */
class PieSwitchListView(ctx:Context):View(ctx) {
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
    class PSVRenderer  {
        var time = 0
        fun render(canvas:Canvas,paint: Paint,v:PieSwitchListView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    data class PieSwitch(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean =  x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
}