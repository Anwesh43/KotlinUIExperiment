package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 03/07/17.
 */
class TappingBallView(ctx:Context):View(ctx) {
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class Ball(var x:Float,var y:Float,var r:Float) {
        var deg:Float = 0.0f
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            paint.color = Color.parseColor("#e53935")
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,180.0f,true,paint)
            paint.color = Color.parseColor("#F5F5F5")
            canvas.drawArc(RectF(-r,-r,r,r),180.0f,180.0f,true,paint)
            canvas.restore()
        }
        fun update() {
            deg += 20.0f
            y += 20.0f
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y <= this.y +r
    }
}