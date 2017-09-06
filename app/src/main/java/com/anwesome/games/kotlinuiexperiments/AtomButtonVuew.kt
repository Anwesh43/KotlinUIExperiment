package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 07/09/17.
 */
class AtomButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class AtomButton(var x:Float,var y:Float,var r:Float,var state:AtomButtonState= AtomButtonState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#009688")
            canvas.drawCircle(0f,0f,r,paint)
            paint.color = Color.WHITE
            paint.strokeWidth = r/30
            for(i in 0..2) {
                canvas.save()
                canvas.rotate(i*30f*state.scale)
                canvas.drawArc(RectF(-r/2,-r/10,r/2,r/10),0f,360f,true,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdate()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class AtomButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdate() {
            dir = 1f-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}