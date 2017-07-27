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
 * Created by anweshmishra on 27/07/17.
 */
class DoubleSideArcButton(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DSABRenderer()
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
    class DSABState {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += dir * 0.2f
            if(scale > 1 || scale < 0) {
                dir = 0
                if(scale >= 1) {
                    scale = 1.0f
                }
                else {
                    scale = 0.0f
                }
            }
        }
        fun stopUpdating():Boolean = dir == 0
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
    }
    data class DoubleSideArc(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#FF5722")
            canvas.save()
            canvas.translate(x,y)
            canvas.drawArc(RectF(-size/2,-size/2*scale,size/2,size/2*scale),0.0f,360.0f,true,paint)
            canvas.restore()
        }
    }
    class DSABAnimationHandler(var dsab:DoubleSideArc,var v:DoubleSideArcButton,var animated:Boolean = false,var state:DSABState= DSABState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            dsab.draw(canvas,paint,state.scale)
        }
        fun update() {
            if(animated) {
                state.update()
                if(state.stopUpdating()) {
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
        fun startUpdating() {
            if(!animated) {
                state.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class DSABRenderer {
        var time = 0
        var handler:DSABAnimationHandler?=null
        fun render(canvas: Canvas,paint:Paint,v:DoubleSideArcButton) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var size = 2*Math.min(w,h)/3
                handler = DSABAnimationHandler(DoubleSideArc(w/2,h/2,size),v)
            }
            handler?.draw(canvas,paint)
            handler?.update()
            time++
        }
        fun handleTap() {
            handler?.startUpdating()
        }
    }
    companion object {
        fun create(activity:Activity) {
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(DoubleSideArcButton(activity),ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
}