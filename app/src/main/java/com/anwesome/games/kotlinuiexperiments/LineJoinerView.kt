package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 28/10/17.
 */

class LineJoinerView(ctx:Context,var n:Int = 5):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = JointRenderer(this)
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
    data class Joint(var i:Int,var x:Float,var y:Float,var size:Float) {
        var state = JointState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#FF9800")
            val scale = state.scale
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            paint.strokeWidth = size/60
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/15,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,(size/15)*scale,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(-60f*(1-scale))
            paint.strokeWidth = size/80
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
    }
    data class JointState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = (prevScale+1)%2
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class JointContainer(var w:Float,var h:Float,var n:Int) {
        var joints:ConcurrentLinkedQueue<Joint> = ConcurrentLinkedQueue()
        var updatingJoints:ConcurrentLinkedQueue<Joint> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val size = (3*w/4)/n
                var x = w/5
                for (i in 0..n - 1) {
                    joints.add(Joint(i,x,h/2,size))
                    x+=size
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            joints.forEach { joint ->
                joint.draw(canvas,paint)
            }
        }
        fun update(view:LineJoinerView,stopcb:()->Unit) {
            updatingJoints.forEach { joint ->
                joint.update()
                if(joint.stopped()) {
                    updatingJoints.remove(joint)
                    if(updatingJoints.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            joints.forEach{ joint ->
                if(joint.handleTap(x,y)) {
                    joint.startUpdating()
                    updatingJoints.add(joint)
                    if(updatingJoints.size == 1) {
                        startcb()
                    }
                }
            }
        }
    }
    class JointAnimator(var container:JointContainer,var view:LineJoinerView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update(view,{
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
        fun handleTap(x:Float,y:Float) {
            container.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
    }
    class JointRenderer(var view:LineJoinerView) {
        var time = 0
        var animator:JointAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0 ){
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = JointAnimator(JointContainer(w,h,view.n),view)
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
        var view:LineJoinerView?=null
        fun create(activity:Activity) {
            view = LineJoinerView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}