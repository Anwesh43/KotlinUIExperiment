package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 04/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class OnCircleButtonView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = OnCircleButtonRenderer(view = this)
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
    data class OnCircleButton(var i:Int,var deg:Float,var size:Float,var cx:Float,var cy:Float,var x:Float = cx.xInCircle(size,i*deg),var y:Float=cy.yInCircle(size,i*deg)) {
        var state = OnCircleButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#311B92")
            paint.style = Paint.Style.STROKE
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,size/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-size/10,-size/10,size/10,size/10),0f,360f*state.scale,true,paint)
            canvas.restore()
            canvas.strokeArc(cx,cy,size,i*deg,deg*state.scale,paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
    }
    data class OnCircleButtonState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = (prevScale+1)%2
                dir = 0f
                prevScale = scale
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
    data class OnCircleButtonContainer(var w:Float,var h:Float,var n:Int) {
        var btns:ConcurrentLinkedQueue<OnCircleButton> = ConcurrentLinkedQueue()
        var updatingBtns:ConcurrentLinkedQueue<OnCircleButton> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val totalR = 2*Math.PI.toFloat()*(Math.min(w,h)/3)
                val deg = 360f/n
                val size = totalR/(1.4f*n)
                for (i in 1..n) {
                    btns.add(OnCircleButton(i,deg,size,w/2,h/2))
                }
            }
        }
        fun update(stopcb:()->Unit) {
            updatingBtns.forEach { updatingBtn ->
                updatingBtn.update()
                if(updatingBtn.stopped()) {
                    updatingBtns.remove(updatingBtn)
                    if(updatingBtns.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            btns.forEach { btn ->
                btn.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            btns.forEach { btn ->
                if(btn.handleTap(x,y)) {
                    btn.startUpdating()
                    updatingBtns.add(btn)
                    if(updatingBtns.size == 1) {
                        startcb()
                    }
                }
            }
        }
    }
    class OnCircleButtonAnimator(var container:OnCircleButtonContainer,var view:OnCircleButtonView){
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update{
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
        fun handleTap(x:Float,y:Float) {
            container.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
    }
    class OnCircleButtonRenderer(var view:OnCircleButtonView,var time:Int = 0) {
        var animator:OnCircleButtonAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = OnCircleButtonAnimator(OnCircleButtonContainer(w,h,view.n),view)
                paint.strokeWidth = Math.min(w,h)/60
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity):OnCircleButtonView {
            val view = OnCircleButtonView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
fun Canvas.strokeArc(x:Float,y:Float,r:Float,startDeg:Float,deg:Float,paint:Paint) {
    paint.style = Paint.Style.STROKE
    var d = startDeg
    val n = 20
    val gap = deg/n
    val path = Path()
    for(i in 1..n) {
        val rx = x+r*Math.cos(d*Math.PI/180).toFloat()
        val ry = x+r*Math.sin(d*Math.PI/180).toFloat()
        if(i == 1) {
            path.moveTo(rx,ry)
        }
        else {
            path.lineTo(rx,ry)
        }
        d += gap
    }
    this.drawPath(path,paint)
}