package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 05/10/17.
 */
class ColorPageSwiperView(ctx:Context,var colors:Array<Int>):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class ColorPageContainer(var w:Float,var h:Float,var colors:Array<Int>,var colorPages:LinkedList<ColorPage> = LinkedList())  {
        init {
            var x = 0f
            colors.forEach { color ->
                colorPages.add(ColorPage(x,w,h,color))
                x += w
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            colorPages.forEach { colorPage ->
                colorPage.draw(canvas,paint)
            }
        }
    }
    data class IndicatorContainer(var x:Float,var y:Float,var size:Float,var n:Int,var i:Int = 0,var currIndex:Int = 0,var prevIndex:Int = -1,var indicators:ConcurrentLinkedQueue<Indicator> = ConcurrentLinkedQueue()) {
        init {
            var curr_x = x - (n/2)*(2*size)
            for(i in 0..n-1) {
                indicators.add(Indicator(curr_x,y,size))
                curr_x += 2*size
            }
        }
        fun shouldAnimate(dir:Int):Boolean {
            return (dir > 0 && currIndex < n-1) || (dir < 0 && currIndex > 0)
        }
        fun startUpdating(dir:Int) {
            prevIndex = currIndex
            currIndex += dir
        }
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            var i = 0
            indicators.forEach { indicator ->
                if(i == currIndex) {
                    indicator.draw(canvas,paint,scale)
                    return
                }
                if(i == prevIndex) {
                    indicator.draw(canvas,paint,1-scale)
                    return
                }
                indicator.draw(canvas,paint,0f)
                i++
            }
        }
    }
    data class ColorPage(var x:Float,var w:Float,var h:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,0f)
            paint.color = color
            canvas.drawRect(RectF(0f,0f,w,h),paint)
            canvas.restore()
        }
    }
    data class Indicator(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            paint.color = Color.WHITE
            canvas.drawCircle(x,y,size/2+size/2*scale,paint)
        }
    }
    data class ColorPageIndicatorContainer(var w:Float,var h:Float,var colors:Array<Int>) {
        var colorPageContainer = ColorPageContainer(w,h,colors)
        var indicatorContainer = IndicatorContainer(w/2,h*0.8f,w/40,colors.size)
        var screen = CPIScreen(w)
        var state = CPIState()
        fun draw(canvas:Canvas,paint:Paint) {
            colorPageContainer.draw(canvas,paint)
            indicatorContainer.draw(canvas,paint,state.scale)
        }
        fun update() {
            state.update()
        }
        fun startUpdating(dir:Int) {
            if(indicatorContainer.shouldAnimate(dir)) {
                screen.startUpdating(dir)
                indicatorContainer.startUpdating(dir)
                state.startUpdating()
            }
        }
        fun stopped():Boolean = state.stopped()
    }
    data class CPIState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1f
        }
        fun stopped():Boolean = dir == 0f
    }
    data class CPIScreen(var w:Float,var x:Float = 0f,var nextX:Float = 0f) {
        fun draw(canvas:Canvas,drawCb:()->Unit,scale:Float) {
            canvas.save()
            canvas.translate(x+nextX*scale,0f)
            drawCb()
            canvas.restore()
        }
        fun startUpdating(dir:Int) {
            nextX = w*dir
        }
    }
    class ColorPageSwiperAnimator(var cpi:ColorPageIndicatorContainer,var view:ColorPageSwiperView,var animated:Boolean = false) {
        fun draw(canvas: Canvas,paint:Paint) {
            cpi.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                cpi.update()
                if(cpi.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleSwipe(dir:Int) {
            cpi.startUpdating(dir)
        }
    }
    class ColorPageSwiperRenderer(var view:ColorPageSwiperView,var time:Int = 0) {
        var animator:ColorPageSwiperAnimator?=null
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ColorPageSwiperAnimator(ColorPageIndicatorContainer(w,h,view.colors),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleSwipe(dir:Int) {
            animator?.handleSwipe(dir)
        }
    }
    class ColorPageSwiper(var renderer:ColorPageSwiperRenderer):GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event:MotionEvent):Boolean = true
        override fun onSingleTapUp(event:MotionEvent):Boolean = true
        override fun onFling(e1:MotionEvent,e2:MotionEvent,velx:Float,vely:Float):Boolean {
            if(Math.abs(velx) > Math.abs(vely)) {
                val dir = velx/Math.abs(velx)
                renderer.handleSwipe(dir.toInt())
            }
            return true
        }
    }
}