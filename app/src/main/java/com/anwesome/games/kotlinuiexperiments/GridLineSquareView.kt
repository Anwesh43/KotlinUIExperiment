package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/09/17.
 */
class GridLineSquareView(ctx:Context,var n:Int = 4):View(ctx) {
    val renderer = GridLineRenderer()
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
    data class GridLineSquare(var x:Float,var y:Float,var w:Float,var h:Float,var n:Int,var state:GridLineState = GridLineState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i*state.scale)
                var x_gap = w/(n+1)
                var cx = x_gap
                for(j in 1..n) {
                    canvas.drawLine(cx-x,-h/2*state.scale,cx-x,h/2*state.scale,paint)
                    cx += x_gap
                }
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class GridLineState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 4.5f
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class GridLineAnimator(var gridLineSquare: GridLineSquare,var view:GridLineSquareView) {
        var animated = false
        fun update() {
            if(animated) {
                gridLineSquare.update()
                if(gridLineSquare.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            gridLineSquare.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class GridLineRenderer {
        var time = 0
        var gridLineAnimator:GridLineAnimator?=null
        fun render(canvas: Canvas,paint:Paint,view:GridLineSquareView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var gridLineSquare = GridLineSquare(w/2,h/2,w,h,view.n)
                gridLineAnimator = GridLineAnimator(gridLineSquare,view)
                paint.color = Color.WHITE
                paint.strokeWidth = w/30
                paint.strokeCap = Paint.Cap.ROUND
            }
            gridLineAnimator?.draw(canvas,paint)
            gridLineAnimator?.update()
        }
        fun handleTap() {
            gridLineAnimator?.handleTap()
        }
    }
}