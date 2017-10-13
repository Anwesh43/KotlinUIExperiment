package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*

/**
 * Created by anweshmishra on 13/10/17.
 */
class ArrowDirectionSquareCreatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        if(event.action == MotionEvent.ACTION_DOWN) {

        }
        return true
    }
    data class ArrowDirectionSquareCreator(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..4) {
                canvas.save()
                canvas.rotate(-90f*i)
                canvas.drawLine(-size/2,-size/2,size/2,-size/2,paint)
                canvas.restore()
            }
            canvas.save()
            canvas.rotate(90f+90f)
            canvas.drawLine(-size/2,-size/2,-size/2+size,-size/2,paint)
            canvas.drawRotatedHorizontalTriangle(-size/2,-size/2+size,0f,size/25,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
    }
}
