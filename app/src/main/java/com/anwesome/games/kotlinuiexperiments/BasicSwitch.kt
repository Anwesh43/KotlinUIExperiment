package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 10/07/17.
 */
class BasicSwitchViewGroup(ctx:Context):ViewGroup(ctx) {
    var w = 0
    var h = 0
    init {
        val displayManager:DisplayManager = ctx.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        var display = displayManager.getDisplay(0)
        var size = Point()
        display?.getRealSize(size)
        this.w = size.x
        this.h = size.y
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var hView = h/30
        var wView = 0
        for(i in 0..childCount-1) {
            var view = getChildAt(i)
            measureChild(view,widthMeasureSpec,heightMeasureSpec)
            hView+=view.measuredHeight
            wView = Math.max(wView,view.measuredWidth)
        }
        setMeasuredDimension(wView+wView/5,hView+w/15)
    }
    override fun onLayout(reloaded:Boolean,a:Int,b:Int,wv:Int,hv:Int) {
        var y = h/30
        var x = w/30
        for(i in 0..childCount-1) {
            var child = getChildAt(i)
            child.layout(x,y,x+child.measuredWidth,y+child.measuredHeight)
            y += child.measuredHeight
        }
    }
}
class BasicSwitchView(ctx:Context):View(ctx) {
    var renderer = SwitchRenderer()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
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
    class SwitchRenderer {
        var drawingController:SwitchDrawingController?=null
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:BasicSwitchView) {
            if(time == 0) {
                drawingController = SwitchDrawingController(canvas.width.toFloat(),canvas.height.toFloat(),v)
            }
            drawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap() {
            drawingController?.startAnimation()
        }
    }
    class SwitchDrawingController(var w:Float,var h:Float,var v:BasicSwitchView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            if(animated) {
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun startAnimation() {
            if(!animated) {
                animated = true
                v.postInvalidate()
            }
        }
    }
}