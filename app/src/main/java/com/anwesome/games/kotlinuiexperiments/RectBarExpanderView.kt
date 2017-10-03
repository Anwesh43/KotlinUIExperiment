package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.ViewGroup

/**
 * Created by anweshmishra on 03/10/17.
 */
class RectBarExpanderView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RectBarExpanderRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class RectBarExpander(var x:Float,var h:Float,var size:Float,var y:Float = h-size,var state:RectBarExpanderState = RectBarExpanderState()){
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#F57F17")
            canvas.save()
            canvas.translate(x,y)
            paint.strokeWidth = h/50
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,size,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*(1-2*i)*state.scale)
                canvas.drawLine(-size/2,0f,size/2,0f,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.drawRect(RectF(x-size,y,x+size,h),paint)
            y = size+(h-2*size)*(1-state.scale)
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
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
    class RectBarExpanderAnimator(var expander:RectBarExpander,var view:RectBarExpanderView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                expander.update()
                if(expander.stopped()) {
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
            expander.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && expander.handleTap(x,y)) {
                animated = true
                expander.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class RectBarExpanderRenderer(var view:RectBarExpanderView,var time:Int = 0) {
        var animator:RectBarExpanderAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = RectBarExpanderAnimator(RectBarExpander(w/2,h,w/20),view)
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
            val view = RectBarExpanderView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}