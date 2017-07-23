package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 23/07/17.
 */
class HorizontalCollapButtonView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer:HCBRenderer = HCBRenderer()
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    class HCBRenderer {
        var time = 0
        var drawingController:HCBDrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:HorizontalCollapButtonView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.strokeWidth = w/60
                paint.strokeCap = Paint.Cap.ROUND
                drawingController = HCBDrawingController(HCBCollapButtonBox(w,h),v)
            }
            drawingController?.draw(canvas,paint)
            drawingController?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.handleTap(x,y)
        }
    }
    class HCBDrawingController(var collapButtonBox:HCBCollapButtonBox,var v:HorizontalCollapButtonView) {
        var animated = false
        var stateContainer = HCBStateContainer()
        fun draw(canvas:Canvas,paint:Paint) {
            collapButtonBox.draw(canvas,paint,stateContainer.scale)
        }
        fun update() {
            if(animated) {
                stateContainer.update()
                if(stateContainer.stopped()) {
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
        fun handleTap(x:Float,y:Float) {
            if(!animated && collapButtonBox.handleTap(x,y)) {
                stateContainer.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class HCBStateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += 0.2f*dir
            if(scale > 1 || scale < 0) {
                dir = 0
                scale = if(scale>1) {
                    1.0f
                }
                else {
                    0.0f
                }
            }
        }
        fun stopped():Boolean = dir == 0
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
    }
    data class HCBCollapButton(var x:Float,var y:Float,var r:Float) {
        fun handleTap(x:Float,y:Float):Boolean {
            var dist = (x-this.x)*(x-this.x)-(y-this.y)*(y-this.y)
            return dist < r*r
        }
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#757575")
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.parseColor("#212121")
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90.0f*i+45.0f*scale)
                canvas.drawLine(0.0f,-3*r/4,0.0f,3*r/4,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
    data class HCBCollapBox(var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            paint.color = Color.parseColor("#01579B")
            canvas.drawRect(RectF(x,y,x+w*scale,y+h),paint)
            canvas.restore()
        }
    }
    data class HCBCollapButtonBox(var w:Float,var h:Float) {
        var collapBox:HCBCollapBox?=null
        var collapButton:HCBCollapButton?=null
        init {
            collapButton = HCBCollapButton(3*w/20,h/20+w/10,w/10)
            collapBox = HCBCollapBox(0.0f,w/5+h/20,w,19*h/20-w/5)
        }
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            collapButton?.draw(canvas,paint,scale)
            collapBox?.draw(canvas,paint,scale)
        }
        fun handleTap(x:Float,y:Float):Boolean = collapButton?.handleTap(x,y)?:false
    }
    companion object {
        fun create(activity:Activity) {
            var size:Point = DimensionsUtil.getDimension(activity)
            activity.addContentView(HorizontalCollapButtonView(activity), ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}