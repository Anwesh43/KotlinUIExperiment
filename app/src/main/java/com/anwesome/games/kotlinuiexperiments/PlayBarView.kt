package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 17/09/17.
 */

class PlayBarView(ctx:Context):View(ctx) {
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
    data class PlayBar(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            val color = Color.parseColor("#d32f2f")
            canvas.save()
            canvas.translate(w/2,h/2)
            drawTriangle(canvas,paint,1f,Color.WHITE,false)
            drawTriangle(canvas,paint,0f,color,true)
            paint.style = Paint.Style.STROKE
            drawCircle(canvas,paint,1f,Color.WHITE)
            drawCircle(canvas,paint,0f,color)
            canvas.restore()
            drawLine(canvas,paint,1f,Color.WHITE)
            drawLine(canvas,paint,0f,color)
        }
        private fun drawTriangle(canvas: Canvas,paint:Paint,scale:Float,color:Int,fill:Boolean) {
            if(fill) {
                paint.style = Paint.Style.FILL
            }
            else {
                paint.style = Paint.Style.STROKE
            }
            val path = Path()
            path.lineTo(-w/11,-h/11)
            path.lineTo(w/11,0f)
            path.lineTo(-w/11,h/11)
            paint.color = color
            canvas.drawPath(path,paint)
        }
        private fun drawLine(canvas: Canvas,paint:Paint,scale:Float,color:Int) {
            canvas.drawLine(0f,4*h/5,w*scale,4*h/5,paint)
        }
        private fun drawCircle(canvas:Canvas,paint:Paint,scale:Float,color:Int) {
            var circlePath = Path()
            val finalDeg = (360*scale).toInt()
            for(i in 0..finalDeg) {
                val cx = ((w/5)*Math.cos((i-90)*Math.PI/180)).toFloat()
                val cy = ((w/5)*Math.sin((i-90)*Math.PI/180)).toFloat()
                if(i == 0) {
                    circlePath.moveTo(cx,cy)
                }
                circlePath.lineTo(cx,cy)
            }
            paint.color = color
            canvas.drawPath(circlePath,paint)
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
}