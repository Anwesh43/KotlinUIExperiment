package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 29/08/17.
 */
class FourColorTriangleView(ctx:Context,var colors:Array<String> = arrayOf("#9c27b0","#03a9f4","#f44336","#4caf50")):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = FCTRenderer()
    var listener:FCTOnOpenCloseListener? = null
    override fun onDraw(canvas:Canvas) {
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
    data class FourColorTriangle(var x:Float,var y:Float,var size:Float,var colors:Array<String>) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            var r = size/10
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/10
            paint.color = Color.parseColor("#ffc107")
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            for(i in 0..3) {
                paint.color = Color.parseColor(colors[i])
                canvas.save()
                canvas.rotate(90f*i)
                canvas.scale(scale,scale)
                var path = Path()
                path.moveTo(0f,0f)
                path.moveTo(-size/2,-size)
                path.lineTo(size/2,-size)
                path.lineTo(0f,0f)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            paint.color = Color.parseColor("#ffc107")
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
    }
    data class FCTState(var scale:Float = 0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1 || scale < 0) {
                dir = 0
                if(scale > 1) {
                    scale = 1f
                }
                if(scale < 0) {
                    scale = 0f
                }
            }
        }
        fun stopUpdating():Boolean = dir == 0
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
    }
    class FCTAnimator(var triangle: FourColorTriangle,var view:FourColorTriangleView,var state:FCTState = FCTState()){
        var animated = false
        fun update() {
            if(animated) {
                state.update()
                if(state.stopUpdating()) {
                    animated = false
                    when(state.scale) {
                        0f -> view.listener?.onCloseListener?.invoke()
                        1f -> view.listener?.onOpenListener?.invoke()
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            triangle.draw(canvas,paint,state.scale)
        }
        fun handleTap() {
            if(!animated) {
                state.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class FCTRenderer {
        var time = 0
        var animator:FCTAnimator?=null
        fun render(canvas:Canvas,paint: Paint,view: FourColorTriangleView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.strokeWidth = w/40
                var triangle = FourColorTriangle(w/2,h/2,Math.min(w,h)/2,view.colors)
                animator = FCTAnimator(triangle,view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        fun create(activity:Activity,vararg listeners:()->Unit) {
            var view = FourColorTriangleView(activity)
            var size = DimensionsUtil.getDimension(activity)
            if(listeners.size == 2) {
                view.listener = FCTOnOpenCloseListener(listeners[0],listeners[1])
            }
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
    data class FCTOnOpenCloseListener(var onOpenListener:()->Unit,var onCloseListener:()->Unit)
}