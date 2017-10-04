package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 05/10/17.
 */
class ColorPageSwiperView(ctx:Context):View(ctx) {
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
        fun draw(canvas:Canvas,paint:Paint) {
            colorPageContainer.draw(canvas,paint)
            indicatorContainer.draw(canvas,paint,0f)
        }
        fun update() {

        }
        fun startUpdating(dir:Int) {

        }
        fun stopped():Boolean = false
    }
}