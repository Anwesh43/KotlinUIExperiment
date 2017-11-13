package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 13/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class ConcentricCircleListView(ctx:Context,var n:Int = 5):View(ctx) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class ConcentricCircle(var x:Float,var y:Float,var r:Float){
        var state:ConcentricCircleState = ConcentricCircleState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(1f,1f)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
    }
    data class ConcentricCircleState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = (prevScale+dir)
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class ConcentricCircleList(var w:Float,var h:Float,var n:Int) {
        var state = ConcentricCircleListState(n)
        val circles:ConcurrentLinkedQueue<ConcentricCircle> = ConcurrentLinkedQueue()
        init {
            for(i in 0..n-1) {
                circles.add(ConcentricCircle(w/2,h/2,Math.min(w,h)/3))
            }
        }
        fun update(stopcb:()->Unit) {
            circles.getAt(state.j)?.update()
            if(circles.getAt(state.j)?.stopped()?:false) {
                state.update()
                if(state.stopped()) {
                    stopcb()
                }
                else {
                    circles.getAt(state.j)?.startUpdating()
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            circles.forEach { circle ->
                circle.draw(canvas,paint)
                state.update()
            }
        }
        fun handleTap(startcb:()->Unit) {
            state.startUpdating()
            circles.getAt(state.j)?.startUpdating()
            startcb()
        }
    }
    data class ConcentricCircleListState(var maxLimit:Int,var j:Int = 0,var dir:Int = 0,var currDir:Int = 1) {
        fun update() {
            j+=dir
            if(j == maxLimit || j == -1) {
                dir = 0
                currDir *= -1
                j+=currDir
            }
        }
        fun stopped():Boolean = dir == 0
        fun startUpdating() {
            dir = currDir
        }
    }
    class ConcentricCircleAnimator(var concentricCircleList:ConcentricCircleList,var view:ConcentricCircleListView) {
        var animated = false
        fun update() {
            if(animated) {
                concentricCircleList.update {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate();
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            concentricCircleList.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
               concentricCircleList.handleTap {
                   animated = true
                   view.postInvalidate()
               }
            }
        }
    }
 }
class ConcentricCircleRenderer(var view:ConcentricCircleListView) {
    var time = 0
    var animator:ConcentricCircleListView.ConcentricCircleAnimator?=null
    fun render(canvas:Canvas,paint:Paint) {
        if(time == 0) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            animator = ConcentricCircleListView.ConcentricCircleAnimator(ConcentricCircleListView.ConcentricCircleList(w,h),view)
        }
        animator?.draw(canvas,paint)
        animator?.update()
        time++
    }
    fun handleTap() {
        animator?.handleTap()
    }
}
fun ConcurrentLinkedQueue<ConcentricCircleListView.ConcentricCircle>.getAt(i:Int):ConcentricCircleListView.ConcentricCircle? {
    var index = 0
    this.forEach { concentricCircle ->
        if(index == i) {
            return concentricCircle
        }
        index++
    }
    return null
}