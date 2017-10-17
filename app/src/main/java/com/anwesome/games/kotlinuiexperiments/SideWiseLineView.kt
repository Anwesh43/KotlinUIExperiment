package com.anwesome.games.kotlinuiexperiments
import android.graphics.*
import android.view.*
import android.content.Context
/**
 * Created by anweshmishra on 17/10/17.
 */
class SideWiseLineView(ctx:Context):View(ctx) {
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
    data class SideWiseLine(var i:Int,var w:Float,var h:Float,var cx:Float = (i%2)*w,var cy:Float = (h/20),var cr:Float = (h/25)) {
        fun draw(canvas:Canvas,paint:Paint) {
            val x = cx+cr*(1-2*i)
            val diffX = (w/2-x)
            canvas.save()
            canvas.translate(x,cy)
            canvas.rotate(45f)
            for(j in 0..1) {
                canvas.save()
                canvas.rotate(90f*j)
                paint.strokeWidth = cr/10
                canvas.drawLine(0f,-(cr*2)/3,0f,(cr*2)/3,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.save()
            canvas.translate(x,h/10)
            var y = 0f
            for(j in 0..9) {
                canvas.drawLine(0f,y,diffX,y,paint)
                y+=(0.9f*h)/10
            }
            canvas.restore()
        }
        fun update(stopCb:()->Unit) {

        }
        fun handleTap(x:Float,y:Float,startCb:()->Unit) {
            if(x>=cx-cr && x<=cx+cr && y>=cy-cr && y<=cy+cr) {
                startCb()
            }
        }
    }
}