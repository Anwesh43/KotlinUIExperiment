package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 19/11/17.
 */
class StackBarsCollapserView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class StackBar(var i:Int,var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.scale(1f-2*i,1f)
            canvas.drawRect(RectF(-w/2*scale,-h/2,0f,h/2),paint)
            canvas.restore()
        }
    }
    class StackBarCollapser(var w:Float,var h:Float,var cx:Float = 0.9f*w,var cy:Float = 0.5f*h,var cr:Float = 0.4f*h) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#9E9E9E")
            canvas.save()
            canvas.drawRect(RectF(0f,0f,w,h),paint)
            canvas.save()
            canvas.translate(cx,cy)
            canvas.rotate(90f*scale)
            paint.color = Color.parseColor("#FAFAFA")
            paint.strokeWidth = cr/18
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-cr,0f,cr,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float) = x>=cx-cr && x<=cx+cr && y>=cy-cr && y<=cy+cr
    }
}