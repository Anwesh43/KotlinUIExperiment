package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
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
    fun addSwitch(listener:OnSwitchSelectionListener) {
        var view:BasicSwitchView = BasicSwitchView(context)
        view.onSelectionListener = listener
        addView(view, LayoutParams(w/4,w/8))
        requestLayout()
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
    var onSelectionListener:OnSwitchSelectionListener?=null
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
                drawingController = SwitchDrawingController(canvas.width.toFloat(),v)
            }
            drawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap() {
            drawingController?.startAnimation()
        }
    }
    class SwitchDrawingController(var w:Float,var v:BasicSwitchView) {
        var animated = false
        var basicSwitch:BasicSwitch?=null
        init {
            basicSwitch = BasicSwitch(w)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            basicSwitch?.draw(canvas,paint)
            if(animated) {
                basicSwitch?.update()
                if(basicSwitch?.dir == 0) {
                    basicSwitch?.handleListener(v?.onSelectionListener)
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
        fun startAnimation() {
            if(!animated) {
                basicSwitch?.startUpdate()
                animated = true
                v.postInvalidate()
            }
        }
    }
    data class BasicSwitch(var w:Float) {
        var dir = 0
        var scale = 0.0f
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            paint.color = Color.parseColor("#BDBDBD")
            canvas.drawRoundRect(RectF(w/10,w/20,9*w/10,w/20+w/10),w/20,w/20,paint)
            paint.color = Color.parseColor("#1565C0")
            canvas.drawRoundRect(RectF(w/10,w/20,w/10+8*w/10*scale,w/20+w/10),w/20,w/20,paint)
            canvas.drawCircle(w/10+8*w/10*scale,w/10,w/10,paint)
            canvas.restore()
        }
        fun update() {
            scale+=dir*0.1f
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
            if(scale < 0) {
                dir = 0
                scale = 0.0f
            }
        }
        fun startUpdate() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
        fun handleListener(listener: OnSwitchSelectionListener?) {
            if(scale >= 1.0f) {
                listener?.onSelect()
            }
            else {
                listener?.onUnSelect()
            }
        }
    }
}
interface OnSwitchSelectionListener {
    fun onSelect() {

    }
    fun onUnSelect() {

    }
}