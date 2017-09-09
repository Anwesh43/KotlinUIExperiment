package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 09/09/17.
 */
class StepButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action == MotionEvent.ACTION_DOWN) {

        }
        return true
    }
    data class StepButton(var w:Float,var h:Float,var x:Float = w/2,var y:Float = 0.9f*h,var r:Float = Math.min(w,h)/25) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f,true,paint)
            canvas.restore()
            val size = Math.min(w,h)/10
            for(i in 1..4) {
                val x = i*(w/5)
                canvas.drawRect(RectF(x-size/2,0.8f*h-(size)*i,x+size/2,0.8f*size),paint)
            }
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x - this.r && x<=this.x+this.r && y>=this.y - this.r && y<=this.y+r
    }
}