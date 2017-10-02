package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.view.View
import android.graphics.*
import android.view.MotionEvent
import java.util.*

/**
 * Created by anweshmishra on 02/10/17.
 */
class PieColorBarView(ctx:Context):View(ctx) {
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
    data class ColorBar(var y:Float,var w:Float,var h:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = color
            canvas.drawRect(RectF(0f,0f,w*scale,h),paint)
        }
    }
    data class PieColorBar(var w:Float,var h:Float,var state:PieColorBarState = PieColorBarState()) {
        var colors:Array<String> = arrayOf("#f44336","#2196F3","#E64A19","#0097A7","#AD1457")
        var colorBars:LinkedList<ColorBar> = LinkedList<ColorBar>()
        init {
            var y = h/5
            var yGap = 0.8f*h/colors.size
            colors.forEach { color ->
                colorBars.push(ColorBar(y,w,h,Color.parseColor(color)))
                y += yGap
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/10)
            paint.color = Color.parseColor("#FDD835")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = Math.min(w,h)/40
            canvas.drawCircle(0f,0f,h/12,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),0f,360f*state.scale,true,paint)
            canvas.restore()
            var i = 0
            colorBars.forEach { colorBar ->
                var scale = state.scale
                if(i%2 == 0) {
                    scale = 1-state.scale
                }
                colorBar.draw(canvas,paint,scale)
                i++
            }
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-h/12 && x<=w/2+h/12 && y>=h/2-h/12 && y<=h/2+h/12
    }
    data class PieColorBarState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class PieColorBarAnimator(var pieColorBar:PieColorBar,var view:PieColorBarView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                pieColorBar.update()
                if(pieColorBar.stopped()) {
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
        fun draw(canvas:Canvas,paint:Paint) {
            pieColorBar.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && pieColorBar.handleTap(x,y)) {
                animated = true
            }
        }
    }
}