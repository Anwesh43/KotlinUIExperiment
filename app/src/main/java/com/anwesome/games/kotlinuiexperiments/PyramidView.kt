package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 05/08/17.
 */
class PyramidView(ctx:Context,var n:Int):View(ctx) {
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
    data class Pyramid(var x:Float,var y:Float,var w:Float,var h:Float,var n:Int)  {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            var size = Math.min(w,h)
            canvas.save()
            canvas.translate(x-w/2,y-h/2)
            var px = 0.0f
            var py = 0.0f
            for(i in 1..n) {
                var nsize = size/i
                canvas.save()
                canvas.translate(px,py)
                paint.color = Color.WHITE
                canvas.drawLine(0f,0f,0f,nsize,paint)
                canvas.drawLine(0f,0f,nsize*(Math.sqrt(3.0).toFloat()/2),nsize/2,paint)
                canvas.drawLine(nsize*(Math.sqrt(3.0).toFloat())/2,nsize/2,nsize,nsize,paint)
                paint.color = Color.parseColor("#00E676")
                canvas.save()
                canvas.translate(0f,nsize/2)
                for(j in 0..1) {
                    canvas.drawLine(0f,0f,0f,(1-2*i)*nsize/2*(scale),paint)
                }
                for(j in 0..1) {
                    canvas.save()
                    canvas.translate(nsize*(Math.sqrt(3.0).toFloat()/2),nsize/2)
                    canvas.rotate((1-2*j)*30.0f)
                    canvas.drawLine(0f,0f,-nsize*scale,0f,paint)
                    canvas.restore()
                }
                canvas.restore()
                canvas.restore()
                px += size/i
                py += (size/(i) - (size/(i+1)))
            }
            canvas.restore()
        }
    }
}