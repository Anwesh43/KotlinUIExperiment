package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 09/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class RotatorDotLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RotatorLineRenderer(this)
    var lineDotSleectionListener:RotateDotLineSelectionListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class RotatorLine(var x:Float,var y:Float,var size:Float,var deg:Float=0f,var prevDeg:Float = 0f) {
        var state = RotatorLineState()
        fun update() {
            state.update()
        }
        fun startUpdating(finalDeg:Float) {
            deg = finalDeg - prevDeg
            state.startUpdating()
        }
        fun stopped():Boolean {
            val stopCondition = state.stopped()
            if(stopCondition) {
                prevDeg += deg
                deg = 0f
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
    data class RotatorLineState(var dir:Float= 0f,var scale:Float = 0f,var prevScale:Float = 0f,var reverse:Boolean = false){
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = (prevScale+1)%2
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            if(dir == 0f) {
                if(reverse) {
                    dir = 1-2*this.scale
                }
                else {
                    dir = 1f
                    scale = 0f
                    prevScale = 0f
                }
            }
        }
        fun stopped():Boolean = dir == 0f
    }
    data class LineDot(var i:Int,var x:Float,var cy:Float,var size:Float,var y:Float = cy+size*(2*i-1),var deg:Float = 90f*(2*i-1)) {
        var state:RotatorLineState = RotatorLineState(reverse = true)
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.strokeWidth = size/25
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,size/10*state.scale,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
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
            curr?.update()
            prev?.update()
            if(line.stopped()) {
                prev = curr
                stopcb()
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            dots.forEach { dot ->
                if(dot.handleTap(x,y)) {
                    curr = dot
                    line.startUpdating(dot.deg)
                    startcb()
                    curr?.startUpdating()
                    prev?.startUpdating()
                    return
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeCap = Paint.Cap.ROUND
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
                    view.lineDotSleectionListener?.selectionListener?.invoke(rotatorLineContainer.curr?.i?:-1)
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
    fun addSelectionListener(selectionListener: (Int) -> Unit) {
        lineDotSleectionListener = RotateDotLineSelectionListener(selectionListener)
    }
    companion object {
        fun create(activity:Activity):RotatorDotLineView {
            val view = RotatorDotLineView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
            return view
        }
    }
    data class RotateDotLineSelectionListener(var selectionListener:(Int)->Unit)
}