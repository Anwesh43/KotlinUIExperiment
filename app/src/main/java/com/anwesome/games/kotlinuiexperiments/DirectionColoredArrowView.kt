package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 30/09/17.
 */
class DirectionColoredArrowView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DirectionColoredArrowRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class DirectionColoredArrow(var w:Float,var h:Float,var color:Int,var state:DirectionColoredArrowState = DirectionColoredArrowState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            drawArrow(canvas,paint)
            canvas.save()
            clipColorPath(canvas)
            paint.color = color
            drawArrow(canvas,paint)
            canvas.restore()
        }
        fun clipColorPath(canvas: Canvas) {
            val path = Path()
            val dir = (state.dir+1)/2
            path.addRect(RectF(0f,h*(1-state.scale)*dir,w,(h*dir+h*state.scale*(1f-dir))),Path.Direction.CW)
            canvas.clipPath(path)
        }
        fun drawArrow(canvas:Canvas,paint:Paint) {
            canvas.drawLine(w/2,h-h/20,w/2,h/20,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w/2,h/20)
                canvas.scale(1f-2*i,1f)
                canvas.drawLine(0f,0f,-w/3,h/5,paint)
                canvas.restore()
            }
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class DirectionColoredArrowState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
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
    class DirectionColoredArrowAnimator(var w:Float,var h:Float,var colors:Array<Int>,var view:DirectionColoredArrowView,var i:Int = 0) {
        var curr:DirectionColoredArrow= DirectionColoredArrow(w,h,colors[i])
        var prev:DirectionColoredArrow?=null
        var animated:Boolean = false
        fun update() {
            if(animated) {
                curr.update()
                prev?.update()
                if(curr.stopped()) {
                    prev = curr
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
            curr.draw(canvas,paint)
            prev?.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                curr = DirectionColoredArrow(w,h,colors[i])
                i++
                if(i >= colors.size ) {
                    i = 0
                }
                animated = true
                view.postInvalidate()
            }
        }
    }

    class DirectionColoredArrowRenderer(var view:DirectionColoredArrowView,var time:Int = 0) {
        var animator:DirectionColoredArrowAnimator?=null
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = DirectionColoredArrowAnimator(w,h, arrayOf(Color.parseColor("#009688"),Color.parseColor("#FF5722"),Color.parseColor("#3949AB"),Color.parseColor("#e53935"),Color.parseColor("#C2185B")),view)
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