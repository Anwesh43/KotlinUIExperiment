package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 20/09/17.
 */
class TwoColorSwitchTriangleView(ctx:Context,var color1:Int = Color.parseColor("#f44336"),var color2:Int = Color.parseColor("#00897B")):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var listener:TwoColorSwitchOnOffListener?=null
    val renderer = TwoSwitchRenderer()
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
    data class TwoColorSwitchTriangle(var x:Float,var y:Float,var size:Float,var color1:Int,var color2:Int,var state:TwoColorSwitchTriangleState = TwoColorSwitchTriangleState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            val path = Path()
            path.moveTo(-size/2,size/2)
            path.lineTo(size/2,size/2)
            path.lineTo(0f,-size/2)
            canvas.save()
            paint.color = color1
            clipRectColorPath(canvas,-size/2,-size/2+size*state.scale)
            canvas.drawPath(path,paint)
            canvas.restore()
            canvas.save()
            paint.color = color2
            clipRectColorPath(canvas,size/2-size*(1-state.scale),size/2)
            canvas.drawPath(path, paint)
            canvas.restore()
            canvas.restore()
        }
        private fun clipRectColorPath(canvas: Canvas,xStart:Float,xEnd:Float) {
            val path = Path()
            path.addRect(RectF(xStart,-size/2,xEnd,size/2),Path.Direction.CW)
            canvas.clipPath(path)
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class TwoColorSwitchTriangleState(var scale:Float = 0f,var deg:Float = 0f,var prevDeg:Float = 0f) {
        fun update() {
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            deg += 4f
            if(deg -prevDeg > 90f) {
                deg = prevDeg+90f
                prevDeg = deg
                scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            }
        }
        fun stopped():Boolean = deg%90 == 0f
    }
    class TwoColorSwitchAnimator(var triangle:TwoColorSwitchTriangle,var view:TwoColorSwitchTriangleView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                triangle.update()
                if(triangle.stopped()) {
                    animated = false
                    when(triangle.state.scale) {
                        0f -> view.listener?.offListener?.invoke()
                        1f -> view.listener?.onListener?.invoke()
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
            triangle.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class TwoSwitchRenderer {
        var time = 0
        var animator:TwoColorSwitchAnimator?= null
        fun render(canvas: Canvas,paint:Paint,view:TwoColorSwitchTriangleView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = TwoColorSwitchAnimator(TwoColorSwitchTriangle(w/2,h/2,0.8f*Math.min(w,h),view.color1,view.color2),view)
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
            var view = TwoColorSwitchTriangleView(activity)
            if(listeners.size == 2){
                view.listener = TwoColorSwitchOnOffListener(listeners[0],listeners[1])
            }
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
    data class TwoColorSwitchOnOffListener(var onListener:()->Unit,var offListener:()->Unit)
}