package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 13/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class ConcentricCircleListView(ctx:Context,var n:Int = 5):View(ctx) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class ConcentricCircle(var x:Float,var y:Float,var r:Float){
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(1f,1f)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
    data class ConcentricCircleState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = (prevScale+dir)
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class ConcentricCircleList(var w:Float,var h:Float,var n:Int) {
        var state = ConcentricCircleListState(n)
        val circles:ConcurrentLinkedQueue<ConcentricCircle> = ConcurrentLinkedQueue()
        fun update(stopcb:()->Unit) {

        }
        fun draw(canvas:Canvas,paint:Paint) {
            circles.forEach { circle ->
                circle.draw(canvas,paint)
                state.update()
            }
        }
        fun handleTap(startcb:()->Unit) {

        }
    }
    data class ConcentricCircleListState(var maxLimit:Int,var j:Int = 0,var dir:Int = 1) {
        fun update() {
            j+=dir
            if(j == maxLimit || j == -1) {
                dir *= -1
                j+=dir
            }
        }
    }
 }
fun ConcurrentLinkedQueue<ConcentricCircleListView.ConcentricCircle>.getAt(i:Int):ConcentricCircleListView.ConcentricCircle? {
    var index = 0
    this.forEach { concentricCircle ->
        if(index == i) {
            return concentricCircle
        }
        index++
    }
    return null
}