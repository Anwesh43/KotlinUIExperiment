package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 23/11/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

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
    data class StepPieContainer(var w:Float,var h:Float,var n:Int) {
        val stepPies:ConcurrentLinkedQueue<StepPie> = ConcurrentLinkedQueue()
        init {
            val x_gap = w/(n+1)
            val y_gap = h/(n+1)
            var x = x_gap/2
            var y = y_gap/2
            for(i in 0..n-1) {
                stepPies.add(StepPie(x,y,x_gap))
                x += x_gap
                y += y_gap
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            stepPies.forEach {
                it.draw(canvas,paint,0f)
            }
        }
        fun update() {

        }
        fun stopped():Boolean = true
        fun startUpdating() {

        }
    }
}