package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 15/07/17.
 */
class DotAndLineView(ctx:Context,var n:Int):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    data class DotLine(var r:Float,var n:Int) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            if(n > 0) {
                var deg = 360.0f/n
                for (i in 0..n) {
                    canvas.save()
                    canvas.rotate(deg*i)
                    paint.style = Paint.Style.STROKE
                    canvas.drawCircle(0.0f,0.0f,r/10,paint)
                    paint.style = Paint.Style.FILL
                    canvas.drawArc(RectF(-r/10,-r/10,r/10,r/10),0.0f,360*scale,true,paint)
                    var xFact = r*Math.cos(deg.toDouble()/2).toFloat()*scale
                    var yFact = r*Math.sin(deg.toDouble()/2).toFloat()
                    canvas.drawLine(-xFact,yFact,xFact,yFact,paint)
                    canvas.restore()
                }
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=-r/10 && x<=r/10 && y>=-r/10 && y<=r/10
    }
    class DALRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,n:Int,v:DotAndLineView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class DALDrawingController(var dotAndLine:DotLine,var v:DotAndLineView) {
        var animated:Boolean = false
        fun draw(canvas:Canvas,paint:Paint) {
            dotAndLine.draw(canvas,paint,1.0f)
            if(animated) {
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (e:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && dotAndLine.handleTap(x,y)) {
                animated = true
                v.postInvalidate()
            }
        }
    }
}
