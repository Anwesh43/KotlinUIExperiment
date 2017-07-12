package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 12/07/17.
 */

class ScaleHorizontalButtonListView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var n = 0
    var renderer = SHBLVRenderer()
    var onClickListener:OnSHBClickListener?=null
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,n,this)
    }
    fun addButton() {
        n++
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class ScaleHorizontalButton(var x:Float,var y:Float,var w:Float,var h:Float) {
        var scale:Float = 0.0f
        var mode:Int = 0
        var waitParam:Int = 0
        fun update() {
            when(mode) {
                1 -> {
                    scale+=0.2f
                    if(scale > 1) {
                        mode = 2
                        scale = 1.0f
                    }
                }
                2 ->  {
                    waitParam++
                    if(waitParam == 5) {
                        waitParam = 0
                        mode = 3
                    }
                }
                3 -> {
                    scale -= 0.2f
                    if(scale < 0) {
                        scale = 0.0f
                        mode = 0
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#9E9E9E")
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(RectF(-w/2,-h/2,w/2,h/2),paint)
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x,y)
                canvas.rotate(i*180.0f)
                paint.color = Color.parseColor("#FFEA00")
                canvas.drawRect(RectF(-w/2,-h/2,-w/2+w/2*scale,h/2),paint)
                canvas.restore()
            }
        }
        fun handleTap(x:Float,y:Float):Boolean {
            val condition = x>=this.x-w/2 && x<=this.x+w/2 && y>=this.y-h/2 && y<=this.y+h/2 && mode == 0
            if(condition) {
                mode = 1
            }
            return condition
        }
        fun stopped():Boolean = mode == 0
    }
    class SHBLVRenderer {
        var time = 0
        var drawingController:DrawingController?=null
        fun render(canvas:Canvas,paint:Paint,n:Int,v:ScaleHorizontalButtonListView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
                drawingController = DrawingController(w.toFloat(),h.toFloat(),n,v)
            }
            drawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.startAnimation(x,y)
        }
    }
    class DrawingController(w:Float,h:Float,n:Int,var v:ScaleHorizontalButtonListView) {
        var animated = false
        var buttons:ConcurrentLinkedQueue<ScaleHorizontalButton> = ConcurrentLinkedQueue()
        var tappedButtons:ConcurrentLinkedQueue<ScaleHorizontalButton> = ConcurrentLinkedQueue()
        init {
            if(n>0) {
                var size = h/(2*n+1)
                var y = 3*size/2
                for (i in 0..n) {
                    buttons.add(ScaleHorizontalButton(w/2,y,w,size))
                    y += 2*size
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            buttons.forEach { button->
                button.draw(canvas,paint)
            }
            if(animated) {
                var i = 0
                tappedButtons.forEach { button->
                    button.update()
                    if(button.mode == 2 && button.scale >= 1) {
                        v.onClickListener?.onClick(i)
                    }
                    if(button.stopped()) {
                        tappedButtons.remove(button)
                        if(tappedButtons.size == 0) {
                            animated = false
                        }
                    }
                    i++
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }

        }
        fun startAnimation(x:Float,y:Float) {
            buttons.forEach { button ->
                if(button.handleTap(x,y)) {
                    tappedButtons.add(button)
                    if(tappedButtons.size == 1) {
                        animated = true
                        v.postInvalidate()
                    }
                }
            }
        }
    }
}
interface OnSHBClickListener {
    fun onClick(index:Int) {

    }
}