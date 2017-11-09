package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class RotatorDotLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class RotatorLine(var x:Float,var y:Float,var size:Float,var deg:Float=0f,var prevDeg:Float = 0f) {
        var state = RotatorLineState()
        fun update() {
            state.startUpdating()
        }
        fun startUpdating(finalDeg:Float) {
            deg = finalDeg - prevDeg
        }
        fun stopped():Boolean {
            val stopCondition = state.stopped()
            if(stopCondition) {
                prevDeg += deg
            }
            return stopCondition
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(prevDeg+deg*state.scale)
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
        }
    }
    data class RotatorLineState(var dir:Float= 0f,var scale:Float = 0f){
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
                scale = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    data class LineDot(var i:Int,var x:Float,var cy:Float,var size:Float,var y:Float = cy+size*(2*i-1),var deg:Float = 90f*(2*i-1)) {
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.strokeWidth = size/25
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,size/10,paint)
            canvas.restore()
        }
    }
    data class RotatorLineContainer(var w:Float,var h:Float,var size:Float = Math.min(w,h)/3,var line:RotatorLine = RotatorLine(w/2,h/2,size)) {
        var dots:ConcurrentLinkedQueue<LineDot> = ConcurrentLinkedQueue()
        var curr:LineDot?=null
        var prev:LineDot?=null
        init {
            for(i in 0..1) {
                dots.add(LineDot(i,w/2,h/2,size))
            }
        }
        fun update(stopcb:()->Unit) {
            line.update()
            if(line.stopped()) {
                stopcb()
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            dots.forEach { dot ->
                if(dot.handleTap(x,y)) {
                    curr = dot
                    line.startUpdating(dot.deg)
                    startcb()
                    return
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#FDD835")
            dots.forEach { dot ->
                dot.draw(canvas,paint)
            }
            line.draw(canvas,paint)
        }
    }
    data class RotatorLineAnimator(var rotatorLineContainer:RotatorLineContainer,var view:RotatorDotLineView) {
        var animated = false
        fun update() {
            if(animated) {
                rotatorLineContainer.update({
                    animated = false
                })

                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            rotatorLineContainer.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                rotatorLineContainer.handleTap(x,y,{
                    animated = true
                    view.postInvalidate()
                })
            }
        }
    }
    class RotatorLineRenderer(var view:RotatorDotLineView,var time:Int = 0) {
        var animator:RotatorLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = RotatorLineAnimator(RotatorLineContainer(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
}