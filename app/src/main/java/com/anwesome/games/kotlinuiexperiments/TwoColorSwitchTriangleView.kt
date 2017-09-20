package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 20/09/17.
 */
class TwoColorSwitchTriangleView(ctx:Context,var color1:Int = Color.parseColor("#f44336"),var color2:Int = Color.parseColor("#00897B")):View(ctx) {
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
    data class TwoColorSwitchTriangle(var x:Float,var y:Float,var size:Float,var color1:Int,var color2:Int,var state:TwoColorSwitchTriangleState = TwoColorSwitchTriangleState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            val path = Path()
            path.moveTo(-size/2,size/2)
            path.lineTo(size/2,size/2)
            path.lineTo(0f,-size/2)
            canvas.save()
            paint.color = color1
            clipRectColorPath(canvas,-size/2,-size/2+size*state.scale)
            canvas.drawPath(path,paint)
            canvas.restore()
            canvas.save()
            paint.color = color2
            clipRectColorPath(canvas,size/2-size*(1-state.scale),size/2)
            canvas.drawPath(path, paint)
            canvas.restore()
            canvas.restore()
        }
        private fun clipRectColorPath(canvas: Canvas,xStart:Float,xEnd:Float) {
            val path = Path()
            path.addRect(RectF(xStart,-size/2,xEnd,size/2),Path.Direction.CW)
            canvas.clipPath(path)
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class TwoColorSwitchTriangleState(var scale:Float = 0f,var deg:Float = 0f,var prevDeg:Float = 0f) {
        fun update() {
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            deg += 4.5f
            if(deg -prevDeg > 90f) {
                deg = prevDeg+90f
                prevDeg = deg
            }
        }
        fun stopped():Boolean = deg%90 == 0f
    }
    class TwoColorSwitchAnimator(var triangle:TwoColorSwitchTriangle,var view:TwoColorSwitchTriangleView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                triangle.update()
                if(triangle.stopped()) {
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
        fun draw(canvas: Canvas,paint:Paint) {
            triangle.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
}