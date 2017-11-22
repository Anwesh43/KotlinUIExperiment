package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 23/11/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class StepPieView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas){

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class StepPie(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawArc(RectF(-size/10,-size/10,size/10,size/10),0f,360f*scale,true,paint)
            canvas.drawLine(0f,0f,size*scale,0f,paint)
            canvas.restore()
        }
    }
}