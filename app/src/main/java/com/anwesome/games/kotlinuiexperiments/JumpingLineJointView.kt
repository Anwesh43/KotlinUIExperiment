package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 20/11/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class JumpingLineJointView(ctx:Context):View(ctx) {
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
    data class JumpingLineJoint(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(w/2,h/2)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f,1f-2*i)
                canvas.drawLine(-w/3,0f,0f,(h/4)*scale,paint)
                canvas.drawLine(w/3,0f,0f,(h/4)*scale,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
    data class JumpingLineJointState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale = Math.sin(deg*Math.PI/180).toFloat()
            deg += 10f
            if(deg > 180) {
                deg = 0f
                scale = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    data class JumpingLineAnimator(var jumpingLineJoint:JumpingLineJoint,var view:JumpingLineJointView) {
        var state = JumpingLineJointState()
        var animated = false
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
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
            jumpingLineJoint.draw(canvas,paint,state.scale)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class JumpingLineRenderer(var view:JumpingLineJointView,var time:Int = 0) {
        var animator:JumpingLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = JumpingLineAnimator(JumpingLineJoint(w,h),view)
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