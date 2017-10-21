package com.anwesome.games.kotlinuiexperiments

import android.content.*
import android.graphics.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 22/10/17.
 */
class CorrespondingButtonPieView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer:CorrespondingButtonPieRenderer = CorrespondingButtonPieRenderer(view = this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class CorrespondingButtonPie(var i:Int,var x:Float,var y:Float,var r:Float) {
        var state = CorrespondingButtonPieState()
        fun draw(canvas:Canvas,paint:Paint,px:Float,py:Float,pr:Float,gap:Float) {
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.translate(px,py)
            canvas.drawArc(RectF(-pr,-pr,pr,pr),i*gap,gap*state.scale,true,paint)
            canvas.restore()
            paint.style = Paint.Style.STROKE
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.scale(state.scale,state.scale)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CorrespondingButtonPieState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale+=0.1f*dir
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class CorrespondingButtonPieContainer(var w:Float,var h:Float,var n:Int,var gap:Float = 0f) {
        var pies:ConcurrentLinkedQueue<CorrespondingButtonPie> = ConcurrentLinkedQueue()
        var updatingPies:ConcurrentLinkedQueue<CorrespondingButtonPie> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val xgap = w/(2*n+1)
                var x = 3*xgap/2
                gap = 360.0f/n
                for (i in 0..n - 1) {
                    pies.add(CorrespondingButtonPie(i,x,h-2*xgap,xgap/2))
                    x+= 2*gap
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            pies.forEach{ pie ->
                pie.draw(canvas,paint,w/2,h/2,Math.min(w,h)/4,gap)
            }
        }
        fun update(stopcb:()->Unit,view:CorrespondingButtonPieView) {
            updatingPies.forEach { pie->
                pie.update()
                if(pie.stopped()) {
                    stopcb()
                }
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            pies.forEach { pie ->
                if(pie.handleTap(x,y)) {
                    startcb()
                }
            }
        }
    }
    class CorrespondingButtonPieAnimator(var container:CorrespondingButtonPieContainer,var view:CorrespondingButtonPieView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update({
                    animated = false
                },view)
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                container.handleTap(x,y,{
                    animated = true
                    view.postInvalidate()
                })
            }
        }
    }
    class CorrespondingButtonPieRenderer(var time:Int = 0,var view:CorrespondingButtonPieView) {
        var animator:CorrespondingButtonPieAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = CorrespondingButtonPieAnimator(CorrespondingButtonPieContainer(w,h,view.n),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
}