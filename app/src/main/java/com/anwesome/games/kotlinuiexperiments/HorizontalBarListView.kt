package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 02/11/17.
 */
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class HorizontalBarListView(ctx:Context,var n:Int = 10):View(ctx) {
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
    data class HorizontalBar(var i:Int,var x:Float,var y:Float,var w:Float,var h:Float) {
        val state = HorizontalBarState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = Math.min(w,h)/40
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45f*state.scale)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-w/3,0f,w/3,paint)
                canvas.restore()
            }
            canvas.drawRect(RectF(-w/2,w/2,w/2,w/2+h*state.scale),paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-w/2 && x<=this.x+w/2 && y>=this.y-w/2 && y<=this.y+w/2
    }
    data class HorizontalBarState(var dir:Float=0f,var prevScale:Float = 0f,var scale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = (prevScale+1)%2
                prevScale = scale
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
    data class HorizontalBarList(var w:Float,var h:Float,var n:Int) {
        var horizontalBars:ConcurrentLinkedQueue<HorizontalBar> = ConcurrentLinkedQueue()
        var prev:HorizontalBar?=null
        var curr:HorizontalBar?=null
        init {
            if(n > 0) {
                val wsize = w / n
                var x = wsize/2
                val y = wsize/2
                for (i in 1..n) {
                    horizontalBars.add(HorizontalBar(i,x,y,wsize,h/2))
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            horizontalBars.forEach {
                it.draw(canvas,paint)
            }
        }
        fun update(stopcb:()->Unit) {
            prev?.update()
            curr?.update()
            if(prev?.stopped()?:true && curr?.stopped()?:false) {
                prev = curr
                curr = null
                stopcb()
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            horizontalBars.forEach { it ->
                if(it.handleTap(x,y)) {
                    curr = it
                    startcb()
                }
            }
        }
    }
}