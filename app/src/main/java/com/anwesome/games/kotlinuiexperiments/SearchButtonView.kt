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
 * Created by anweshmishra on 16/08/17.
 */
class SearchButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = SearchButtonRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class SearchButton(var x:Float,var y:Float,var w:Float,var h:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45+deg)
            canvas.drawLine(0.0f,h/3,0.0f,0.0f,paint)
            canvas.drawCircle(0.0f,-h/6,h/6,paint)
            canvas.restore()
        }
        fun update(scale:Float) {
            deg = -90*scale
            x = h/2+(w-4*h/3)*scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-h/2 && x<=this.x+h/2 && y>=this.y-h/2 && y<=this.y+h/2
    }
    data class SearchButtonState(var scale:Float = 0.0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
            if(scale < 0) {
                dir = 0
                scale = 0.0f
            }
        }
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
        fun stopped():Boolean = dir == 0
    }
    class SearchButtonDrawingController(var searchButton:SearchButton,var view:SearchButtonView,var state:SearchButtonState = SearchButtonState(),var animated:Boolean = false) {
        fun update() {
            if(animated) {
                state.update()
                searchButton.update(state.scale)
                if(state.stopped()) {
                    animated = false
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
            searchButton.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(searchButton.handleTap(x,y)) {
                state.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class SearchButtonRenderer {
        var time = 0
        var controller:SearchButtonDrawingController?=null
        fun render(canvas: Canvas,paint:Paint,v:SearchButtonView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.color = Color.WHITE
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = w/80
                paint.strokeCap = Paint.Cap.ROUND
                controller = SearchButtonDrawingController(SearchButton(h/2,h/2,w,h),v)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = SearchButtonView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y/12))
        }
    }
}