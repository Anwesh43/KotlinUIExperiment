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
    data class StepButton(var w:Float,var h:Float,var x:Float = w/2,var y:Float = 0.9f*h,var r:Float = Math.min(w,h)/25,var state:StepButtonState = StepButtonState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,true,paint)
            canvas.restore()
            val size = Math.min(w,h)/10
            for(i in 1..4) {
                val x = i*(w/5)
                canvas.drawRect(RectF(x-size/2,0.8f*h-(size)*i*state.scale,x+size/2,0.8f*size),paint)
            }
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = true
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x - this.r && x<=this.x+this.r && y>=this.y - this.r && y<=this.y+r
    }
    data class StepButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update(){
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                dir = 0f
                scale = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
    class StepButtonAnimator(var stepButton:StepButton,var view:StepButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                stepButton.update()
                if(stepButton.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            stepButton.draw(canvas, paint)
        }
        fun handleTap(x: Float,y:Float) {
            if(!animated && stepButton.handleTap(x,y)) {
                stepButton.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }

    }
}