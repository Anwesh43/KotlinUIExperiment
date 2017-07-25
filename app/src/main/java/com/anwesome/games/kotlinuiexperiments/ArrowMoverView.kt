package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
/**
 * Created by anweshmishra on 25/07/17.
 */
class ArrowMoverView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer:AMVRenderer = AMVRenderer()
    override fun onDraw(canvas: Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    class AMVRenderer {
        var renderingController:AMVRenderingController?=null
        var time = 0
        fun render(canvas: Canvas,paint:Paint,v:ArrowMoverView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var size = Math.min(w,h)/10
                renderingController = AMVRenderingController(ArrowMover(w/2,h-size/2,size,h-size),v)
            }
            renderingController?.draw(canvas,paint)
            renderingController?.animate()
            time++
        }
        fun handleTap() {
            renderingController?.startAnimation()
        }
    }
    class AMVStateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += dir*0.2f
            if(scale > 1 || scale <0) {
                dir = 0
                if(scale > 1) {
                    scale = 1.0f
                }
                else {
                    scale = 0.0f
                }
            }
        }
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
        fun stopped():Boolean = dir == 0
    }
    data class ArrowMover(var x:Float,var y:Float,var size:Float,var h:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y-h*scale)
            canvas.rotate(180.0f*scale)
            var path = Path()
            path.moveTo(0.0f,0.0f)
            path.lineTo(-size/2,size/2)
            path.lineTo(size/2,size/2)
            path.lineTo(0.0f,-size/2)
            canvas.drawPath(path,paint)
            canvas.restore()
        }
    }
    class AMVRenderingController(var arrowMover:ArrowMover,var v:ArrowMoverView,var state:AMVStateContainer = AMVStateContainer(),var animated:Boolean = false) {
        fun draw(canvas: Canvas,paint:Paint) {
            arrowMover.draw(canvas,paint,state.scale)
        }
        fun animate() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    v.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun startAnimation() {
            if(!animated) {
                animated = true
                state.startUpdating()
                v.postInvalidate()
            }
        }
    }
    companion object {
        fun create(activity: Activity) {
            var view = ArrowMoverView(activity)
            activity.setContentView(view)
        }
    }
}