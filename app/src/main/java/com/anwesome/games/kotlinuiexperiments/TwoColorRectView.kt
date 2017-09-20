package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 21/09/17.
 */
class TwoColorRectView(ctx:Context):View(ctx) {
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
    data class TwoColorRect(var x:Float,var y:Float,var size:Float,var state:TwoColorState = TwoColorState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            val a = (size/Math.sqrt(2.0)).toFloat()
            canvas.save()
            canvas.translate(x,y)
            clipCirclePathAndDrawRect(0f,360f*state.scale,a,Color.parseColor("#f44336"),canvas,paint)
            clipCirclePathAndDrawRect(360f*state.scale,360f*(1-state.scale),a,Color.parseColor("#2196F3"),canvas,paint)
            canvas.restore()
        }
        private fun clipCirclePathAndDrawRect(degStart:Float,degSweep:Float,a:Float,color:Int,canvas: Canvas,paint:Paint) {
            canvas.save()
            val path = Path()
            path.moveTo(0f,0f)
            for(deg in degStart.toInt()..(degStart+degSweep).toInt()) {
                val x = Math.cos(deg*Math.PI/180).toFloat()
                val y = Math.sin(deg*Math.PI/180).toFloat()
                paint.color = Color.parseColor("#f44336")
                canvas.drawRect(RectF(-a,-a,a,a),paint)
                path.lineTo(x,y)
            }
            canvas.clipPath(path)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class TwoColorState(var scale:Float=0f,var dir:Float = 0f,var animated:Boolean = false) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 1f
            }
        }
        fun startUpdating() {
            dir = 1f-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}