package com.anwesome.games.kotlinuiexperiments
import android.content.Context
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 08/10/17.
 */
class DirectionIndicatingArcView(ctx:Context):View(ctx) {
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
    data class DirectionIndicatingArc(var x:Float,var y:Float,var r:Float,var deg:Float,var scale:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*scale,true,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float) = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class DirectionIndicatingArcContainer(var w:Float,var h:Float,var arcs:ConcurrentLinkedQueue<DirectionIndicatingArc> = ConcurrentLinkedQueue()) {
        var prev:DirectionIndicatingArc?=null
        var curr:DirectionIndicatingArc?=null
        var gapDeg = 0f
        init {
            val size = 2*Math.min(w,h)/5
            val circlePoint:(Int)->PointF = {i -> PointF(size*Math.cos(i*Math.PI/2).toFloat(),size*Math.sin(i*Math.PI/2).toFloat())}
            for(i in 0..3) {
                val point = circlePoint(i)
                val arc = DirectionIndicatingArc(point.x,point.y,size/5,i*90f)
                arcs.add(arc)
                if(i == 0) {
                    prev = arc
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            arcs.forEach { arc ->
                arc.draw(canvas,paint)
            }
        }
        fun update() {
        }
        fun stopped():Boolean = true
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            arcs.forEach { arc ->
                if(arc.handleTap(x,y) && arc != prev) {
                    gapDeg = arc.deg - (prev?.deg?:0f)
                    curr = arc
                    startcb()
                }
            }
        }
    }
    data class DIAState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                dir = 0f
                scale = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}