package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 27/09/17.
 */
class DoubleArrowTapMoverView(ctx:Context):View(ctx) {
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
    data class DoubleArrowTapMover(var x:Float,var y:Float,var size:Float,var maxGap:Float = 0f,var state:DoubleArrowTapMoverState = DoubleArrowTapMoverState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(90f*(state.scale*(1-state.mode)))
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f,2*i-1f)
                canvas.save()
                canvas.translate(0f,maxGap*(state.scale*state.mode))
                canvas.drawPath(arrowPath(),paint)
                canvas.restore()
                canvas.restore()
            }
            canvas.restore()
        }
        fun arrowPath():Path {
            val path = Path()
            path.moveTo(-size/2,0f)
            path.lineTo(0f,-size/2)
            path.lineTo(size/2,-size/2)
            return path
        }
        fun stopped():Boolean = state.stopped()
        fun update() {
            state.update()
        }
        fun handleTap(x:Float,y:Float):Boolean = x >= this.x - size/2 && x <= this.x+size/2 && y >= this.y -size/2 && y <= this.y+size/2
    }
    data class DoubleArrowTapMoverState(var mode:Int = 0,var scale:Float = 0f) {
        fun update() {
            scale += 0.1f
            if(scale > 1) {
                mode ++
                scale = 0f
            }
        }
        fun stopped():Boolean = mode == 2
    }
}