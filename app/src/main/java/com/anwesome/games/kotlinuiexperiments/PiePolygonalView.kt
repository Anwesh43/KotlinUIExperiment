package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 24/08/17.
 */
class PiePolygonalView(ctx:Context,var n:Int = 3):View(ctx) {
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
    data class PiePolygonal(var w:Float,var h:Float,var n:Int,var scale:Float = 0.0f,var r:Float = Math.min(w,h)/10) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = Math.min(w,h)/50
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0.0f,360.0f*scale,true,paint)
            var deg = 360.0f/n
            for(i in 1..n) {
                canvas.save()
                var x_gap =  (w/3*Math.cos((i*deg/2)*Math.PI/180)).toFloat()
                var y_gap = (w/3*Math.sin((i*deg/2)*Math.PI/180)).toFloat()
                canvas.drawLine(-x_gap*scale,y_gap,x_gap*scale,y_gap,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float) = x>=w/2-r && x<=w/2+r && y>=h/2-r && y<=h/2+r
    }
    class PiePolygonalRenderer(var view:PiePolygonalView) {
        var time = 0
        var piePolygonal:PiePolygonal?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                piePolygonal = PiePolygonal(w,h,view.n)
            }
            piePolygonal?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            if(piePolygonal?.handleTap(x,y)?:false) {

            }
        }
        fun update(scale:Float) {
            piePolygonal?.scale = scale
            view.postInvalidate()
        }
    }
}