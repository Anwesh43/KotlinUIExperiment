package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 02/11/17.
 */
import android.app.Activity
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

class HorizontalBarListView(ctx:Context,var n:Int = 10):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = HorizontalBarRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class HorizontalBar(var i:Int,var x:Float,var y:Float,var w:Float,var h:Float) {
        val state = HorizontalBarState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = Math.min(w,h)/20
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            canvas.rotate(45f*state.scale)
            paint.color = Color.parseColor("#FAFAFA")
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-w/3,0f,w/3,paint)
                canvas.restore()
            }
            canvas.restore()
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawRoundRect(RectF(-w/3,w/2,w/3,w/2+h*state.scale),Math.min(w,h)/5,Math.min(w,h)/5,paint)
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
                dir = 0f
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
                    x+=wsize
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
                    if(it != prev) {
                        curr = it
                        curr?.startUpdating()
                        prev?.startUpdating()
                        startcb()
                    }
                    return
                }
            }
        }
    }
    class HorizontalBarAnimator(var list:HorizontalBarList,var view:HorizontalBarListView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                list.update({
                    animated = false
                })
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            list.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                list.handleTap(x,y,{
                    animated = true
                    view.postInvalidate()
                })
            }
        }
    }
    class HorizontalBarRenderer(var view:HorizontalBarListView,var time:Int = 0) {
        var animator:HorizontalBarAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = HorizontalBarAnimator(HorizontalBarList(w,h,view.n),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object {
        var view:HorizontalBarListView?=null
        fun create(activity:Activity) {
            view = HorizontalBarListView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}