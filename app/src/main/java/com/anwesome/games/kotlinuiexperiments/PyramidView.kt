package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 05/08/17.
 */
class PyramidView(ctx:Context,var n:Int):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = PVRenderer()
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
    data class Pyramid(var x:Float,var y:Float,var w:Float,var h:Float,var n:Int)  {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            var size = Math.min(w,h)
            canvas.save()
            canvas.translate(x-w/2,y-h/2)
            var px = 0.0f
            var py = 0.0f
            for(i in 1..n) {
                var nsize = size/i
                canvas.save()
                canvas.translate(px,py)
                paint.color = Color.WHITE
                canvas.drawLine(0f,0f,0f,nsize,paint)
                canvas.drawLine(0f,0f,nsize*(Math.sqrt(3.0).toFloat()/2),nsize/2,paint)
                canvas.drawLine(nsize*(Math.sqrt(3.0).toFloat())/2,nsize/2,nsize,nsize,paint)
                paint.color = Color.parseColor("#00E676")
                canvas.save()
                canvas.translate(0f,nsize/2)
                for(j in 0..1) {
                    canvas.drawLine(0f,0f,0f,(1-2*i)*nsize/2*(scale),paint)
                }
                for(j in 0..1) {
                    canvas.save()
                    canvas.translate(nsize*(Math.sqrt(3.0).toFloat()/2),nsize/2)
                    canvas.rotate((1-2*j)*30.0f)
                    canvas.drawLine(0f,0f,-nsize*scale,0f,paint)
                    canvas.restore()
                }
                canvas.restore()
                canvas.restore()
                px += size/i
                py += (size/(i) - (size/(i+1)))
            }
            canvas.restore()
        }
    }
    class PVState {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += dir * 0.2f
        }
        fun startUpdating() {
            if(dir == 0) {
                dir = when(scale) {
                    0.0f -> 1
                    1.0f -> -1
                    else -> dir
                }
            }
        }
        fun stopped():Boolean = dir == 0
    }
    class PVRenderingController(var pyramid:Pyramid,var v:PyramidView,var animated:Boolean = false,var state:PVState = PVState()) {
        fun update() {
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
        fun draw(canvas:Canvas,paint:Paint) {
            pyramid.draw(canvas,paint,state.scale)
        }
        fun handleTap() {
            if(!animated) {
                state.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class PVRenderer {
        var time = 0
        var controller:PVRenderingController?=null
        fun render(canvas:Canvas,paint:Paint,v:PyramidView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var sumN = 0f
                for(i in 1..v.n) {
                    sumN += 1/i
                }
                sumN *= (Math.sqrt(3.0)/2).toFloat()
                controller = PVRenderingController(Pyramid(w/2,h/2,w,w/sumN,v.n),v)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun handleTap() {
            controller?.handleTap()
        }
    }
    companion object {
        fun create(activity:Activity,n:Int = 4) {
            var view = PyramidView(activity,n)
            var size = DimensionsUtil.getDimension(activity)
            activity.setContentView(view)
        }
    }
}