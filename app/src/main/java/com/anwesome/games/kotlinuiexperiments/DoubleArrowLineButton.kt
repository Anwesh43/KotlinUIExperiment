package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 10/08/17.
 */
class DoubleArrowLineButton(ctx:Context):View(ctx) {
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
    data class ArrowLine(var x:Float,var y:Float,var w:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            canvas.drawLine(x,y,x+w*scale,y,paint)
        }
    }
    data class DoubleArrow(var x:Float,var y:Float,var size:Float,var w:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x+w*scale,y)
            canvas.rotate(180.0f*scale)
            for(i in 0..1) {
                canvas.save()
                canvas.translate(size/2*i,0.0f)
                var path = Path()
                path.moveTo(0.0f,0.0f)
                path.lineTo(-size/2,-size/2)
                path.lineTo(-size/2,size/2)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
    data class DoubleLineArrow(var doubleArrow:DoubleArrow,var arrowLine:ArrowLine) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            doubleArrow.draw(canvas,paint,scale)
            arrowLine.draw(canvas,paint,scale)
        }
        fun handleTap(x:Float,y:Float):Boolean = doubleArrow.handleTap(x,y)
    }
}