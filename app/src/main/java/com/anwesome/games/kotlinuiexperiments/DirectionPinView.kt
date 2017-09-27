package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 28/09/17.
 */
class DirectionPinView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DPRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x)
            }
        }
        return true
    }
    data class DirectionPin(var w:Float,var h:Float,var dir:Float,var x:Float = w/2,var y:Float = h/2,var size:Float = h/6,var r:Float = h/15) {
        var state = DirectionPinState()
        fun draw(canvas:Canvas,paint:Paint) {
            if(state.scales.size == 4) {
                canvas.save()
                canvas.translate(x, y)
                canvas.rotate(90 * dir*state.scales[2])
                canvas.save()
                canvas.translate(0f, w / 2*state.scales[3])
                canvas.drawLine(0f, 0f, 0f, -size*state.scales[1], paint)
                canvas.drawCircle(0f, -size*state.scales[1], r*state.scales[0], paint)
                canvas.restore()
                canvas.restore()
            }
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class DirectionPinState(var j:Int = 0) {
        var scales:Array<Float> = arrayOf(0f,0f,0f,0f)
        fun update() {
            if(j < scales.size) {
                scales[j] += 0.1f
                if (scales[j] > 1) {
                    j++
                }
            }
        }
        fun stopped():Boolean = j == scales.size
    }
    class DPAnimator(var w:Float,var h:Float,var view:DirectionPinView,var  directionPins:ConcurrentLinkedQueue<DirectionPin> = ConcurrentLinkedQueue()) {
        var animated = false
        fun update() {
            if(animated) {
                directionPins.forEach { updatingPin ->
                    updatingPin.update()
                    if(updatingPin.stopped()) {
                        animated = false
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            directionPins.forEach { directionPin ->
                directionPin.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float) {
            if(x!=w/2) {
                var dir = (x - w / 2) / Math.abs(x - w / 2)
                directionPins.add(DirectionPin(w, h, dir))
                if (directionPins.size == 1) {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    class DPRenderer(var view:DirectionPinView,var time:Int = 0) {
        var animator:DPAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = DPAnimator(w,h,view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float) {
            animator?.handleTap(x)
        }
    }
    companion object {
        fun create(activity:Activity) {
            val view = DirectionPinView(activity)
            activity.setContentView(view)
        }
    }
}