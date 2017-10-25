package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 25/10/17.
 */
class RectEdgeView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{

            }
        }
        return true
    }
    data class RectEdge(var i:Int,var r:Float,var x:Float = r*Math.cos(i*Math.PI/2+Math.PI/4).toFloat() ,var y:Float = r*Math.sin(i*Math.PI/2+Math.PI/4).toFloat()) {
        var state = RectEdgeState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r/10,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.drawCircle(0f,0f,(r/10)*state.scale,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(90f*i+90*state.scale)
            canvas.drawLine(0f,0f,0f,r/Math.sqrt(2.0).toFloat(),paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating(){
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x >= this.x - r/10 && x<=this.x+r/10 && y>=this.y-r/10 && y<=this.y+r/10
    }
    data class RectEdgeState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
}