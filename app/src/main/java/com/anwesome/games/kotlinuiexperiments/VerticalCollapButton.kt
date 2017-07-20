package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 21/07/17.
 */
class VerticalCollapButton(ctx:Context):View(ctx) {
    val renderer:VCBRenderer = VCBRenderer()
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    class VCBRenderer {
        var time = 0
        var renderingController:VCBRenderingController?=null
        fun render(canvas:Canvas,paint:Paint,v:VerticalCollapButton) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.strokeWidth = w/40
                paint.strokeCap = Paint.Cap.ROUND
                renderingController = VCBRenderingController(CollapVerticalButtonShape(w,h),v)
            }
            renderingController?.draw(canvas,paint)
            time++
            renderingController?.animate()
        }
        fun handleTap(x:Float,y:Float) {
            renderingController?.handleTap(x,y)
        }
    }
    class VCBStateContainer(var scale:Float=0.0f,var dir:Int = 0) {
        fun update() {
            scale += dir*0.2f
            if(scale > 1.0f) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
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
    class VCBRenderingController(var verticalButtonShape: CollapVerticalButtonShape,var v:VerticalCollapButton) {
        var animated = false
        var stateContainer = VCBStateContainer()
        fun animate() {
            if(animated) {
                stateContainer.update()
                if(stateContainer.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            verticalButtonShape.draw(canvas,paint,stateContainer.scale)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                animated = true
                stateContainer.startUpdating()
                v.postInvalidate()
            }
        }
    }
    data class CollapButton(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.BLACK
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90.0f*i+45.0f*scale)
                canvas.drawLine(0.0f,-2*r/3,0.0f,2*r/3,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
    data class VerticalButton(var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawRect(RectF(-w/2,0.0f,w/2,h*scale),paint)
            canvas.restore()
        }
    }
    class CollapVerticalButtonShape(w:Float,h:Float) {
        var collapButton:CollapButton?=null
        var verticalButton:VerticalButton?=null
        init{
            collapButton = CollapButton(w/2,w/10,w/10)
            verticalButton = VerticalButton(w/2,w/5,w/6,h-w/5)
        }
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            collapButton?.draw(canvas,paint,scale)
            verticalButton?.draw(canvas,paint,scale)
        }
    }
    companion object {
        fun create(activity:Activity) {
            var v =  VerticalCollapButton(activity)
            var dimension = DimensionsUtil.getDimension(activity)
            activity.addContentView(v, ViewGroup.LayoutParams(dimension.x/2,dimension.x/2))
        }
    }
}