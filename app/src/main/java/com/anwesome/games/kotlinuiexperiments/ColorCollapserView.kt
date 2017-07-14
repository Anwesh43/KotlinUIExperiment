package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 14/07/17.
 */
class ColorCollapserView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class CCVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:ColorCollapserView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    class DrawingController(w:Float,h:Float,var v:ColorCollapserView) {
        init {

        }
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            if(animated) {
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                animated = true
                v.postInvalidate()
            }
        }
    }
    class StateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale+=dir*0.1f
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
        fun stopped():Boolean = dir == 0
    }
    data class Collapser(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,deg:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawCircle(0,0,size,paint)
            paint.color = Color.WHITE
            for(i in 0..2) {
                canvas.save()
                canvas.translate(0.0f,-size/6)
                canvas.rotate(45.0f*(2*i-1))
                canvas.drawLine(0.0f,0.0f,0.0f,size/6,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size && x<=this.x+size && y>=this.y-size && y<=this.y+size
    }
    data class ColorPlate(var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#0D47A1")
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(0.0f,0.0f,w,h*scale),paint)
            canvas.restore()
        }
    }
}