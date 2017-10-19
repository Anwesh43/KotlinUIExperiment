package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 20/10/17.
 */
class ArrowTipRotatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ArrowTipRenderer(view=this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                renderer.handleTap()
            }
        }
        return true
    }
    data class ArrowTipRotator(var x:Float,var y:Float,var size:Float) {
        var state:ArrowTipRotatorState = ArrowTipRotatorState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = size/12
            paint.color = Color.parseColor("#FAFAFA")
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
        fun handleTap() {
            if(!animated) {
                arrowTipRotator.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class ArrowTipRenderer(var time:Int = 0,var view:ArrowTipRotatorView) {
        var animator:ArrowTipRotatorAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ArrowTipRotatorAnimator(ArrowTipRotator(w/2,h/2,Math.min(w,h)/5),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
}