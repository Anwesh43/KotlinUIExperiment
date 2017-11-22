package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 23/11/17.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class StepPieView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = StepPieRenderer(this)
    override fun onDraw(canvas:Canvas){
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class StepPie(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.strokeWidth = size/25
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/10,paint)
            canvas.drawArc(RectF(-size/10,-size/10,size/10,size/10),0f,360f*scale,true,paint)
            canvas.drawLine(0f,0f,size*scale,0f,paint)
            canvas.restore()
        }
    }
    data class StepPieContainer(var w:Float,var h:Float,var n:Int) {
        val state = StepPieState()
        val stepPies:ConcurrentLinkedQueue<StepPie> = ConcurrentLinkedQueue()
        init {
            val x_gap = w/(n+1)
            val y_gap = h/(n+1)
            var x = x_gap/2
            var y = y_gap/2
            for(i in 0..n-1) {
                stepPies.add(StepPie(x,y,x_gap))
                x += x_gap
                y += y_gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            stepPies.forEach {
                it.draw(canvas,paint,state.scale)
            }
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
    }
    data class StepPieState(var scale:Float = 0f,var dir:Float=0f,var prevScale:Float = 0f) {
        fun update(){
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class StepPieAnimator(var container:StepPieContainer,var view:StepPieView) {
        var animated:Boolean = false
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update()
                if(container.stopped()) {
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
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class StepPieRenderer(var view:StepPieView,var time:Int = 0) {
        var animator:StepPieAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.width.toFloat()
                animator = StepPieAnimator(StepPieContainer(w,h,view.n),view)
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = Color.parseColor("#FF9800")
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
        fun create(activity:Activity):StepPieView {
            val view = StepPieView(activity)
            activity.setContentView(view)
            return view
        }
    }
}