package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 28/09/17.
 */
class DirectionPinView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    data class DirectionPin(var w:Float,var h:Float,var dir:Float,var x:Float = w/2,var y:Float = h/2,var size:Float = h/6,var r:Float = h/15) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(90*dir)
            canvas.save()
            canvas.translate(0f,w/2)
            canvas.drawLine(0f,0f,0f,-size,paint)
            canvas.drawCircle(0f,-size,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
    }
    data class DirectionPinState(var scales:Array<Float> = arrayOf(0f,0f,0f,0f),var j:Int = 0) {
        fun update() {
            if(j < scales.size) {
                scales[j] += 0.1f
                if (scales[j] > 1) {
                    j++
                }
            }
        }
        fun stopped():Boolean = j == scales.size
    }
}