package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

/**
 * Created by anweshmishra on 03/10/17.
 */
class RectBarExpanderView(ctx:Context):View(ctx) {
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
    data class RectBarExpander(var x:Float,var h:Float,var size:Float,var y:Float = h-size){
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#F57F17")
            canvas.save()
            canvas.translate(x,y)
            paint.strokeWidth = h/50
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,size,paint)
            canvas.save()
            canvas.rotate(90f)
            canvas.drawLine(-size/2,0f,size/2,0f,paint)
            canvas.restore()
            canvas.restore()
            canvas.drawRect(RectF(x-size,y,x+size,h),paint)
            y = size+(h-2*size)*(1)
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size && x<=this.x+size && y>=this.y-size && y<=this.y+size
    }
    data class RectBarExpanderState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            this.scale += dir*0.1f
            if(this.scale > 1) {
                this.scale = 1f
                this.dir = 0f
            }
            if(this.scale < 0) {
                this.scale = 0f
                this.dir = 0f
            }
        }
        fun startUpdating() {
            this.scale = 1-2*this.dir
        }
        fun stopped():Boolean = dir == 0f
    }
}