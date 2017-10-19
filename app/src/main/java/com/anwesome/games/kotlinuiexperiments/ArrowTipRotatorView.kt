package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 20/10/17.
 */
class ArrowTipRotatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class ArrowTipRotator(var x:Float,var y:Float,var size:Float) {
        var state:ArrowTipRotatorState = ArrowTipRotatorState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = size/12
            canvas.save()
            canvas.translate(x,y)
            for(j in 0..1) {
                canvas.save()
                canvas.rotate(180f*j*state.scale)
                canvas.drawLine(0f, 0f, 0f, -size, paint)
                canvas.save()
                canvas.translate(0f, -size)
                for (i in 0..1) {
                    canvas.save()
                    canvas.rotate((2 * i - 1) * 45f)
                    canvas.drawLine(0f, 0f, 0f, size / 4, paint)
                    canvas.restore()
                }
                canvas.restore()
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class ArrowTipRotatorState(var scale:Float=0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0f) {
                dir = 0f
                scale = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class ArrowTipRotatorAnimator(var arrowTipRotator:ArrowTipRotator,var view:ArrowTipRotatorView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                arrowTipRotator.update()
                if(arrowTipRotator.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            arrowTipRotator.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                arrowTipRotator.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
}