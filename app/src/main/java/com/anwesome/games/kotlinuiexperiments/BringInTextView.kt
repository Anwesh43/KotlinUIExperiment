package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 16/11/17.
 */

class BringInTextView(ctx:Context,var text:String="Hello"):View(ctx) {
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
    data class BringInText(var w:Float,var h:Float,var text:String) {
        var state = BringInTextState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawRoundRect(RectF(-w/3,-h/15,w/3,h/15),h/10,h/10,paint)
            canvas.save()
            canvas.scale(state.scale,state.scale)
            paint.color = Color.parseColor("#00E5FF")
            canvas.drawRoundRect(RectF(-w/3,-h/15,w/3,h/15),h/10,h/10,paint)
            canvas.restore()
            paint.textSize = h/15
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f-2*i,1f)
                canvas.drawText(text,-w/2+(w/2)*state.scale,(1f-2*i)*(h/5),paint)
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
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2 - w/3 && x<=w/2+w/3 && y>=h/2-h/15 && y<=h/2+h/15

    }
    data class BringInTextState(var scale:Float=0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = prevScale+dir
                prevScale = scale
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class BringInTextAnimator(var bringInText:BringInText,var view:BringInTextView) {
        var animated = false
        fun update() {
            if(animated) {
                bringInText.update()
                if(bringInText.stopped()) {
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
            bringInText.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class BringInTextRenderer(var view:BringInTextView,var time:Int = 0) {
        var animator:BringInTextAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = BringInTextAnimator(BringInText(w,h,view.text),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity) {
            val view = BringInTextView(activity)
            activity.setContentView(view)
        }
    }
}