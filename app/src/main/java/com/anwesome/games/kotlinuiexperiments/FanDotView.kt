package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 29/10/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class FanDotView(ctx:Context):View(ctx) {
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
    data class FanDot(var x:Float,var y:Float,var r:Float,var size:Float,var prevDeg:Float  = 0f) {
        var state = FanDotState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x, y)
            canvas.strokeAndScaleFillCircle(0f, 0f, r*state.scales[0],1f, paint)
            canvas.drawInATriangle {
                canvas.strokeAndScaleFillCircle(0f,-size,r*state.scales[0],1f,paint)
            }
            canvas.save()
            canvas.rotate(prevDeg+120f*state.scales[2])
            canvas.drawInATriangle {
                canvas.drawLine(0f,0f,0f,-size*state.scales[1],paint)
            }
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update({
                prevDeg += 120f
                prevDeg %= 360
            })
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class FanDotState(var j:Int=0,var dir:Float = 0f) {
        val scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update(stopcb:()->Unit) {
            scales[j] += 0.1f*dir
            if(scales[j] > 1) {
                scales[j] = 1f
                if(j == scales.size-1) {
                    scales[j] = 0f
                    stopcb()
                    j--
                    dir = -1f
                }
                else {
                    j++
                }
            }
            else if(scales[j] < 0) {
                scales[j] = 0f
                if(j == 0) {
                    dir = 0f
                }
                else {
                    j--
                }
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    class FanDotAnimator(var fanDot:FanDot,var view:FanDotView) {
        var animated = false
        fun update() {
            if(animated) {
                fanDot.update()
                if(fanDot.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            fanDot.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                fanDot.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class FanDotRenderer(var view:FanDotView,var time:Int = 0) {
        var animator:FanDotAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = FanDotAnimator(FanDot(w/2,h/2,Math.min(w,h)/25,Math.min(w,h)/4),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
}
fun Canvas.drawInATriangle(drawCb:()->Unit) {
    for(i in 0..2) {
        this.save()
        this.rotate(120f*i)
        drawCb()
        this.restore()
    }
}
fun Canvas.strokeAndScaleFillCircle(x:Float,y:Float,r:Float,scale:Float,paint:Paint) {
    paint.style = Paint.Style.STROKE
    this.drawCircle(x,y,r,paint)
    paint.style = Paint.Style.FILL
    this.drawCircle(x,y,r*scale,paint)
}