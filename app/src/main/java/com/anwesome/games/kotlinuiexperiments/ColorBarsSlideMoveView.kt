package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.view.*
import android.graphics.*
/**
 * Created by anweshmishra on 27/10/17.
 */
class ColorBarSlideMoveView(ctx:Context):View(ctx) {
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
    data class ColorSlideMove(var i:Float,var x:Float,var y:Float,var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(0f,0f,w,h),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
}
