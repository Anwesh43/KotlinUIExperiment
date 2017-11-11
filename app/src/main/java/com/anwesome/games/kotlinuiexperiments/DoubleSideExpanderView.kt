package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 11/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class DoubleSideExpanderView(ctx:Context):View(ctx) {
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
    data class SideExpander(var i:Int,var w:Float,var h:Float,var x:Float=w/2-(w/4+w/10)*(2*i-1),var y:Float=h/2) {
        var state = SideExpanderState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,w/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w/10,-w/10,w/10,w/10),0f,360f*state.scale,true,paint)
            canvas.restore()
            val rx = w/2-w/4+i*(w/4)
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.scale(state.scale,1f)
            canvas.drawRect(RectF(rx-w/2,-w/8,rx-w/4,w/8),paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-w/10 && x<=this.x+w/10 && y>=this.y-w/10 && y<=this.y+w/10
    }
    data class SideExpanderState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = (prevScale+1)%2
                prevScale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class DoubleSideExapnder(var w:Float,var h:Float) {
        var sideExpanders = ConcurrentLinkedQueue<SideExpander>()
        var tappedExpanders = ConcurrentLinkedQueue<SideExpander>()
        init {
            for(i in 0..1) {
                sideExpanders.add(SideExpander(i,w,h))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            sideExpanders.forEach{ sideExpander ->
                sideExpander.draw(canvas,paint)
            }
        }
        fun update(stopcb:()->Unit) {
            tappedExpanders.forEach { tappedExpander ->
                tappedExpander.update()
                if(tappedExpander.stopped()) {
                    if(tappedExpanders.remove(tappedExpander)) {
                        if(tappedExpanders.size == 0) {
                            stopcb()
                        }
                    }
                }
            }
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
}