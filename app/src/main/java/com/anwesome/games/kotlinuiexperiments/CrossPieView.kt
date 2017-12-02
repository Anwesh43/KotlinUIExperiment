package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 02/12/17.
 */
import android.view.*
import android.content.*
import android.graphics.*

class CrossPieView(ctx:Context):View(ctx) {
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
    data class CrossPie(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(w/2,h/10)
            paint.color = Color.parseColor("#ff9800")
            paint.strokeWidth = Math.min(w,h)/50
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,h/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-h/12,-h/12,h/12,h/12),0f,360f*scale,true,paint)
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.parseColor("#673ab7")
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w / 2, 3 * h / 5)
                canvas.rotate(-45f-90f*i)
                canvas.drawLine(0f, -w / 3, 0f, -w / 3 + 2 * w / 3 * scale,paint)
                canvas.restore()
            }
        }
    }
    data class CrossPieContainer(var w:Float,var h:Float) {
        var crossPie = CrossPie(w,h)
        var state = CrossPieContainerState()
        fun draw(canvas:Canvas,paint:Paint) {
            crossPie.draw(canvas,paint,state.scale)
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class CrossPieContainerState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
    data class CrossPieRenderer(var container:CrossPieContainer,var view:CrossPieView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex: Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                container.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class CrossPieController(var view:CrossPieView,var time:Int = 0) {
        var renderer:CrossPieRenderer?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                renderer = CrossPieRenderer(CrossPieContainer(w,h),view)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            renderer?.draw(canvas,paint)
            renderer?.update()
            time++
        }
        fun handleTap() {
            renderer?.handleTap()
        }
    }
}