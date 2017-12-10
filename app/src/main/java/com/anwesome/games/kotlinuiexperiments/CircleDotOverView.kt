package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 10/12/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class CircleDotOverView(ctx:Context,var n:Int = 10):View(ctx) {
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
    data class CircleDot(var i:Int,var x:Float,var y:Float,var r:Float,var size:Float,var deg:Float) {
        val state = CircleDotState()
        fun draw(canvas:Canvas,paint:Paint) {
            val new_deg = deg*i
            val update_deg = deg*state.scale
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(new_deg+update_deg)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(size,0f,r,paint)
            canvas.restore()
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/2
            canvas.drawArc(RectF(-size,-size,size,size),new_deg,update_deg,false,paint)
        }
        fun update(stopcb:(Int,Float)->Unit) {
            state.update{scale ->
                stopcb(i,scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class CircleDotState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
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
    data class CircleDotContainer(var w:Float,var h:Float,var n:Int) {
        val dots:ConcurrentLinkedQueue<CircleDot> = ConcurrentLinkedQueue()
        val state = CircleDotContainerState(n)
        init {
            if(n>0) {
                val deg = 360f/n
                val size = Math.min(w,h)/3
                val r = (2*Math.PI*size).toFloat()/(n*7)
                for(i in 0..n) {
                    dots.add(CircleDot(i,w/2,h/2,r,size,deg))
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#4A148C")
            dots.forEach { dot ->
                dot.draw(canvas,paint)
            }
            paint.color = Color.parseColor("#00E676")
            state.executeFn { j ->
                val scale = dots.at(j)?.state?.scale?:0f
                canvas.drawLine(w/10,4*h/5,w/10+(4*w/5)*scale,4*h/5,paint)
            }
        }
        fun update(stopcb:(Int,Float)->Unit) {
            state.executeFn {j ->
               dots.at(j)?.update(stopcb)
                state.update()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.executeFn { j->
                dots.at(j)?.startUpdating(startcb)
            }
        }
    }
    data class CircleDotOverAnimator(var container:CircleDotContainer,var view:CircleDotOverView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{j,scale ->
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
            canvas.drawColor(Color.parseColor("#212121"))
            container.draw(canvas,paint)
        }
        fun startUpdating() {
            if(animated) {
                container.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    data class CircleDotContainerState(var n:Int,var j:Int = 0,var dir:Int = 1) {
        fun update() {
            j += dir
            if(j == n || j == -1) {
                dir*=-1
                j += dir
            }
        }
        fun executeFn(cb:(Int)->Unit) {
            cb(j)
        }
    }
}
fun ConcurrentLinkedQueue<CircleDotOverView.CircleDot>.at(i:Int):CircleDotOverView.CircleDot? {
    var index = 0
    this.forEach { it ->
        if(i == index) {
            return it
        }
        index++
    }
    return null
}