package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class AlternateLinePieView(ctx:Context,var n:Int = 6):View(ctx) {
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
    data class AlternateLine(var i:Int,var x:Float,var y:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#00E676")
            canvas.save()
            canvas.translate(x,y)
            when(i%2) {
                0 -> canvas.drawLine(0f,0f,0f,h*scale,paint)
                1 -> canvas.drawLine(0f,h,0f,h*(1-scale),paint)
            }
            canvas.restore()
        }
    }
    data class AlternateLineContainer(var w:Float,var h:Float,var n:Int) {
        var j = 0
        var lines:ConcurrentLinkedQueue<AlternateLine> = ConcurrentLinkedQueue()
        init {
            val gap = w/(n+1)
            var x = gap
            for(i in 0..n-1) {
                lines.add(AlternateLine(i,x,0f,4*h/5))
                x += gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            if(n > 0) {
                val degGap = 360f/n
                paint.strokeWidth = (lines.getAt(0)?.x?:10f)/10
                canvas.save()
                canvas.translate(w/2,h/10)
                canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),j*degGap,degGap,true,paint)
                canvas.restore()
                canvas.save()
                canvas.translate(0f,h/5)
                lines.forEach { line ->
                    line.draw(canvas,paint,1f)
                }
                canvas.restore()
            }
        }
        fun update(stopcb:()->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class AlternateLineState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1f - 2*scale
        }
    }
}
fun ConcurrentLinkedQueue<AlternateLinePieView.AlternateLine>.getAt(i:Int):AlternateLinePieView.AlternateLine? {
    var index = 0
    this.forEach {
        if(i == index) {
            return it
        }
        index++
    }
    return null
}