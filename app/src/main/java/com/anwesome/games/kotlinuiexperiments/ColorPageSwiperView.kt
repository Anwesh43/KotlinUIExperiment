package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

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
    data class ColorPageContainer(var w:Float,var h:Float) {

    }
    data class IndicatorContainer(var x:Float,var y:Float,var size:Float,var n:Int,var i:Int = 0) {

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
}