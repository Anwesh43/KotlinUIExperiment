package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 31/08/17.
 */
class BarPieLoaderView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class BarShape(var y:Float,var w:Float,var h:Float,var state:BarShapeState = BarShapeState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            var r = Math.min(w,h)/20
            canvas.save()
            canvas.translate(w/20,y+h/2)
            paint.style = Paint.Style.STROKE
            canvas.drawRoundRect(RectF(0f,-h/2,w*.9f,h/2),r,r,paint)
            canvas.scale(state.scale,1f)
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(RectF(0f,-h/2,w*.9f,h/2),r,r,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.w/20 && x<=this.w && y>=this.y && y<=this.y+h
        fun stopped():Boolean = state.stopped()
    }
    data class BarShapeState(var scale:Float=0f,var dir:Float = 0f) {
        fun update() {
            scale+=dir*0.2f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
}