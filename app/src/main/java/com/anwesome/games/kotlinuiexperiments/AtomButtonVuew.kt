package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 07/09/17.
 */
class AtomButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = AtomButtonRenderer()
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class AtomButton(var x:Float,var y:Float,var r:Float,var state:AtomButtonState= AtomButtonState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#009688")
            canvas.drawCircle(0f,0f,r,paint)
            paint.color = Color.WHITE
            paint.strokeWidth = r/30
            paint.style = Paint.Style.STROKE
            for(i in 0..2) {
                canvas.save()
                canvas.rotate(i*60f*state.scale)
                canvas.drawArc(RectF(-3*r/4,-r/5,3*r/4,r/5),0f,360f,true,paint)
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
    class AtomButtonAnimator(var atomButtom:AtomButton,var view:AtomButtonView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            atomButtom.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                atomButtom.update()
                if(atomButtom.stopped()) {
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
        fun startUpdating() {
            if(!animated) {
                animated = true
                atomButtom.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class AtomButtonRenderer {
        var time = 0
        var atomButtonAnimator:AtomButtonAnimator?=null
        fun render(canvas: Canvas,paint: Paint,view:AtomButtonView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var atomButton = AtomButton(w/2,h/2,0.4f*Math.min(w,h))
                atomButtonAnimator = AtomButtonAnimator(atomButton,view)
            }
            atomButtonAnimator?.draw(canvas,paint)
            atomButtonAnimator?.update()
            time++
        }
        fun handleTap() {
            atomButtonAnimator?.startUpdating()
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = AtomButtonView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}