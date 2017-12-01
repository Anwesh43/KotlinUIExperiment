package com.anwesome.games.kotlinuiexperiments

import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 01/12/17.
 */

class StackRectsView(ctx:Context):View(ctx) {
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
    data class StackRect(var x:Float,var y:Float,var size:Float,var dest:Float,var py:Float=y) {
        var state = StackRectState()
        fun draw(canvas:Canvas,paint:Paint) {
            y = py+(dest-py)*state.scale
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class StackRectState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += dir*0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f - 2*scale
            startcb()
        }
    }
    data class StackRectContainer(var w:Float,var h:Float,var n:Int) {
        var state = StackRectContainerState(n)
        var stackRects:ConcurrentLinkedQueue<StackRect> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                var size = (2*h/3)/n
                var dest = h-size/2
                for(i in 0..n-1) {
                    stackRects.add(StackRect(w/2,-size/2,size,dest))
                    dest -= size
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            var deg = 30
            val scale = stackRects.getAt(state.j)?.state?.scale?:0f
            if(n > 0) {
                deg = 360/n
            }
            canvas.save()
            canvas.translate(w/10,h/2)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,w/10,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-w/10,-w/10,w/10,w/10),0f,deg*state.j+deg*scale,true,paint)
            canvas.restore()
            stackRects.forEach { stackRect ->
                stackRect.draw(canvas,paint)
            }
        }
        fun update(stopcb:(Int,Float)->Unit) {
            stackRects.getAt(state.j)?.update{ scale ->
                stopcb(state.j,scale)
                state.incrementCounter()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            stackRects.getAt(state.j)?.startUpdating(startcb)
        }
    }
    data class StackRectContainerState(var n:Int,var j:Int = 0,var dir:Int = 0,var prevDir:Int = 1) {
        fun incrementCounter() {
            j += prevDir
            dir = 0
            if(j == n || j == -1) {
                prevDir *= -1
                j+= prevDir
            }
        }
    }
}
fun ConcurrentLinkedQueue<StackRectsView.StackRect>.getAt(i:Int):StackRectsView.StackRect? {
    var index = 0
    this.forEach {
        if(i == index) {
            return it
        }
        index++
    }
    return null
}