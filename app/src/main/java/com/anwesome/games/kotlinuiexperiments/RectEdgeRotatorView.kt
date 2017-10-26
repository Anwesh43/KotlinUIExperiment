package com.anwesome.games.kotlinuiexperiments

import android.graphics.*
import android.view.*
import android.content.*
/**
 * Created by anweshmishra on 26/10/17.
 */
class RectEdgeRotatorView(ctx:Context):View(ctx) {
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
    data class RectEdgeRotator(var x:Float,var y:Float,var size:Float){
        var state = RectEdgeRotatorState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            canvas.rotate(-30f*state.scales[2])
            canvas.drawLine(0f,0f,size*state.scales[0],0f,paint)
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(-30f*i)
                canvas.drawCircle(0f,size,(size/6)*state.scales[1],paint)
                canvas.restore()
            }
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
    data class RectEdgeRotatorState(var i:Int = 0,var dir:Int = 0,var currDir:Int = 1) {
        var scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update() {
            scales[i]+=dir*0.1f
            if(scales[i]>1 && dir == 1) {
                dir = 0
                scales[i] = 1f
            }
            if(scales[i] < 0 && dir == -1) {
                dir = 0
                scales[i] = 0f
            }
            if(dir == 0) {
                i += currDir
                if(i == scales.size || i == -1) {
                    currDir *= -1
                    i+=currDir
                }
            }
        }
        fun startUpdating() {
            dir = currDir
        }
        fun stopped():Boolean = dir == 0
    }
    class RERAnimator(var rotator:RectEdgeRotator,var view:RectEdgeRotatorView) {
        var animated = false
        fun update() {
            if(animated) {
                rotator.update()
                if(rotator.stopped()) {
                    animated = true
                    view.invalidate()
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
                rotator.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            rotator.draw(canvas,paint)
        }
    }
}