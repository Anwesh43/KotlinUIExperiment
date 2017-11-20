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
}