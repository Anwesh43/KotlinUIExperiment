package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/10/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue
val lineColors = arrayOf("#00C853","#f44336","#FFEA00","#00838F","#3F51B5")
class ColoredLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class ColoredLine(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            val adjustedSize = size/4+(size/4)
            paint.strokeWidth = size/10
            canvas.save()
            canvas.translate(x,y)
            canvas.drawLine(0f,-adjustedSize,0f,adjustedSize,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
    data class ColoredLineContainer(var w:Float,var h:Float) {
        val coloredLines:ConcurrentLinkedQueue<ColoredLine> = ConcurrentLinkedQueue()
        val updatingLines:ConcurrentLinkedQueue<ColoredLine> = ConcurrentLinkedQueue()
        var animated = false
        init {
            if(lineColors.size > 0) {
                var gap = Math.min(w,h)/5
                var x = w/2-(gap/10)*(coloredLines.size.toFloat()/2)
                val y = h/2-gap/2
                for (i in 0..lineColors.size - 1) {
                    coloredLines.add(ColoredLine(x,y,gap))
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            coloredLines.forEach { coloredLine ->
                coloredLine.draw(canvas,paint)
            }
        }
        fun update(stopcb:()->Unit) {
            if(animated) {
                updatingLines.forEach { updatingLine ->
                    updatingLine.update()
                    if(updatingLine.stopped()) {
                        updatingLines.remove(updatingLine)
                        if(updatingLines.size == 0) {
                            stopcb()
                        }
                    }
                }
            }
        }
    }
}