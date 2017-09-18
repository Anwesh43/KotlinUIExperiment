package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 19/09/17.
 */

class GtoBView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = GToBRenderer()
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class GtoBCircle(var x:Float,var y:Float,var r:Float,var state:GtoBState = GtoBState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            clipRectPath(-r,-r+2*r*(1-state.scale),canvas)
            paint.color = Color.GREEN
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.save()
            clipRectPath(r-2*r*(state.scale),r,canvas)
            paint.color = Color.BLUE
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        private fun clipRectPath(yStart:Float,yEnd:Float,canvas: Canvas) {
            val path = Path()
            path.addRect(RectF(-r,yStart,r,yEnd),Path.Direction.CW)
            canvas.clipPath(path)
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class GtoBState(var scale:Float = 0f,var dir:Float=0f) {
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
            dir = 1f-2*scale
        }
    }
    class GToBRenderer {
        var time = 0
        var animator:GToBAnimator?=null
        fun render(canvas:Canvas,paint:Paint,view:GtoBView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = GToBAnimator(GtoBCircle(w/2,h/2,w/3),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    class GToBAnimator(var circle:GtoBCircle,var view:GtoBView) {
        var animated = false
        fun draw(canvas: Canvas,paint:Paint) {
            circle.draw(canvas,paint)
        }
        fun update() {
            circle.update()
            if(circle.stopped()) {
                animated = false
            }
            if(animated) {
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(circle.handleTap(x,y) && !animated) {
                animated = true
                circle.startUpdating()
                view.postInvalidate()
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = GtoBView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}