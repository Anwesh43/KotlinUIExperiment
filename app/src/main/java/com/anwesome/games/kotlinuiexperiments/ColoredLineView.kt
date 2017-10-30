package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/10/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

val lineColors = arrayOf("#00C853", "#f44336", "#FFEA00", "#00838F", "#3F51B5")

class ColoredLineView(ctx: Context) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class ColoredLine(var x: Float, var y: Float, var size: Float) {
        var state = ColoredLineState()
        fun draw(canvas: Canvas, paint: Paint) {
            val adjustedSize = size / 4 + (size / 4)*state.scale
            paint.strokeWidth = size / 10
            canvas.save()
            canvas.translate(x, y)
            canvas.drawLine(0f, -adjustedSize, 0f, adjustedSize, paint)
            canvas.restore()
        }

        fun update(stopcb: () -> Unit) {
            state.update(stopcb)
        }
    }

    data class ColoredLineContainer(var w: Float, var h: Float) {
        var j = 0
        val coloredLines: ConcurrentLinkedQueue<ColoredLine> = ConcurrentLinkedQueue()
        var curr: ColoredLine? = null
        var animated = false

        init {
            if (lineColors.size > 0) {
                var gap = Math.min(w, h) / 5
                var x = w / 2 - (gap / 10) * (coloredLines.size.toFloat() / 2)
                val y = h / 2 - gap / 2
                for (i in 0..lineColors.size - 1) {
                    coloredLines.add(ColoredLine(x, y, gap))
                }
            }
        }

        fun draw(canvas: Canvas, paint: Paint) {
            coloredLines.forEach { coloredLine ->
                coloredLine.draw(canvas, paint)
            }
        }

        fun update(stopcb: () -> Unit) {
            curr?.update({
                j++
                if (j == coloredLines.size) {
                    j = 0
                    stopcb()
                } else {
                    curr = coloredLines.getAt(j)
                }
            })
        }

        fun handleTap(startcb: () -> Unit) {
            if (j == 0) {
                curr = coloredLines.getAt(0)
                startcb()
            }
        }
    }

    data class ColoredLineState(var scale: Float = 0f, var deg: Float = 0f) {
        fun update(stopcb: () -> Unit) {
            scale = Math.abs(Math.sin(deg * Math.PI / 180).toFloat())
            deg += 4.5f
            if (deg > 180) {
                deg = 0f
                stopcb()
            }
        }
    }
}

fun ConcurrentLinkedQueue<ColoredLineView.ColoredLine>.getAt(index: Int): ColoredLineView.ColoredLine? {
    var i = 0
    this.forEach {
        if (i == index) {
            return it
        }
        i++
    }
    return null
}