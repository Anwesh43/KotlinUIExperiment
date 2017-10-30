package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 30/10/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

val lineColors = arrayOf("#00C853", "#f44336", "#FFEA00", "#00838F", "#3F51B5")

class ColoredLineView(ctx: Context) : View(ctx) {
    val renderer = ColoredLineRenderer(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class ColoredLine(var i:Int,var x: Float, var y: Float, var size: Float) {
        var state = ColoredLineState()
        fun draw(canvas: Canvas, paint: Paint) {
            val adjustedSize = size / 4 + (size / 4)*state.scale
            paint.color = Color.parseColor(lineColors[i])
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

        init {
            if (lineColors.size > 0) {
                var gap = Math.min(w, h) / 5
                var x = w / 2 - (gap / 10) * (coloredLines.size.toFloat() / 2)
                val y = h / 2 - gap / 2
                for (i in 0..lineColors.size - 1) {
                    coloredLines.add(ColoredLine(i,x, y, gap))
                    x+=(gap/10)
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
    class ColoredLineAnimator(var container:ColoredLineContainer,var view:ColoredLineView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update({
                    animated = false
                })
                try {
                    Thread.sleep(10)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                container.handleTap {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    class ColoredLineRenderer(var view:ColoredLineView) {
        var time = 0
        var animator:ColoredLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ColoredLineAnimator(ColoredLineContainer(w,h),view)
                paint.strokeCap = Paint.Cap.ROUND
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        var view:ColoredLineView?=null
        fun create(activity:Activity) {
            view = ColoredLineView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
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