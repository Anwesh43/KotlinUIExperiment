package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 20/07/17.
 */
class BiDirecLoaderButtonView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer = BDLBVRenderer()
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
    class BDLBVRenderer {
        var time = 0
        var animationHandler:AnimationHandler?=null
        fun render(canvas:Canvas,paint:Paint,v:BiDirecLoaderButtonView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.strokeWidth = (w/50)
                paint.style = Paint.Style.STROKE
                animationHandler = AnimationHandler(BiDirecLoader(w/2,h/2,Math.min(w,h)/3),v)
            }
            animationHandler?.draw(canvas,paint)
            animationHandler?.animate()
            time++
        }
        fun handleTap() {
            animationHandler?.startAnimating()
        }
    }
    data class BiDirecLoader(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            var deg = 360*scale
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.parseColor("#311B92")
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,deg,false,paint)
            canvas.drawArc(RectF(-r,-r,r,r),180.0f,deg,false,paint)
            canvas.restore()
        }
    }
    class StateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
    }
    class AnimationHandler(var biDirecLoader: BiDirecLoader,var v:BiDirecLoaderButtonView) {
        var stateContainer = StateContainer()
        var animated = true
        fun draw(canvas:Canvas,paint:Paint) {
            biDirecLoader.draw(canvas,paint,stateContainer.scale)
        }
        fun animate() {
            if(animated) {
                stateContainer.update()
                if(stateContainer.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                } catch (ex: Exception) {

                }
            }
        }
        fun startAnimating() {
            if(!animated) {
                stateContainer.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            var size = getDimension(activity)
            var v = BiDirecLoaderButtonView(activity)
            activity.addContentView(v,ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
        private fun getDimension(activity:Activity):Point {
            var displayManager:DisplayManager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            var display:Display = displayManager.getDisplay(0)
            var point = Point()
            display.getRealSize(point)
            return point
        }
    }
}