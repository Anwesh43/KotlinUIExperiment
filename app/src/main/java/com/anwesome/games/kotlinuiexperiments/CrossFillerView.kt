package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 31/10/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
class CrossFillerView(ctx:Context):View(ctx) {
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
    data class CrossFiller(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45f)
            paint.strokeWidth = size/30
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(i*90f)
                paint.color = Color.GRAY
                canvas.drawLine(0f,0f,0f,-size/2,paint)
                paint.color = Color.BLUE
                canvas.drawLine(0f,-size/2+size/2,0f,-size/2,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
    data class CrossFillerState(var dir:Int = 0,var j:Int = 0,var prevScale:Float = 0f,var currDir:Int = 1) {
        var scales:Array<Float> = arrayOf(0f,0f)
        fun update() {
            scales[j] += dir*0.1f
            if(Math.abs(scales[j]-prevScale) > 1) {
                scales[j] = (prevScale+1)%2
                prevScale = scales[j]
                if(j == scales.size || j == -1) {
                    currDir *= -1
                    j+=currDir
                    dir = 0
                }
                else {
                    j+=dir
                }
            }
        }
        fun startUpdating() {
            dir = currDir
        }
        fun stopped():Boolean = dir == 0
    }
}