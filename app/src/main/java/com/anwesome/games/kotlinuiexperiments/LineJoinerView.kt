package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 28/10/17.
 */

class LineJoinerView(ctx:Context,var n:Int = 5):View(ctx) {
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
    data class Joint(var i:Int,var x:Float,var y:Float,var size:Float) {
        var state = JointState()
        fun draw(canvas:Canvas,paint:Paint) {
            val scale = state.scale
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            paint.strokeWidth = size/60
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/15,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0f,0f,(size/15)*state.scale,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(-60f*(1-state.scale))
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
                var x = 0f
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
    }
}