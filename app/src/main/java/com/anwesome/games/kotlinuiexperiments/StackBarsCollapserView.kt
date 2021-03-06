package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 19/11/17.
 */
class StackBarsCollapserView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = StackBarCollapserRenderer(this)
    var clickListener:StackBarClickListener?=null
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
    class StackBar(var i:Int,var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#f44336")
            var p = i%2
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.scale(1f-2*p,1f)
            canvas.drawRect(RectF(-w/2*scale,-h/2,0f,h/2),paint)
            canvas.restore()
        }
    }
    class StackBarCollapser(var w:Float,var h:Float,var cx:Float = 0.9f*w,var cy:Float = 0.5f*h,var cr:Float = 0.4f*h) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#9E9E9E")
            canvas.save()
            canvas.drawRect(RectF(0f,0f,w,h),paint)
            canvas.save()
            canvas.translate(cx,cy)
            canvas.rotate(90f*scale)
            paint.color = Color.parseColor("#FAFAFA")
            paint.strokeWidth = cr/5
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-cr,0f,cr,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float) = x>=cx-cr && x<=cx+cr && y>=cy-cr && y<=cy+cr
    }
    class StackBarsCollapser(var w:Float,var h:Float, var n:Int,var collapser:StackBarCollapser = StackBarCollapser(w/2,h/10)) {
        val state = StackBarCollapserState()
        var bars:LinkedList<StackBar> = LinkedList()
        init {
            for(i in 0..n-1) {
                bars.add(StackBar(i,w/2,h/10))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            var y = h/2-(h/10)*(n+1)/2
            canvas.save()
            canvas.translate(w/4,y)
            collapser.draw(canvas,paint,state.scale)
            var by = h/10
            bars.forEach { bar ->
                canvas.save()
                canvas.translate(0f,by)
                bar.draw(canvas,paint,state.scale)
                canvas.restore()
                by += h/10
            }
            canvas.restore()
        }
        fun update(stopcb:()->Unit) {
            state.update()
            if(state.stopped()) {
                stopcb()
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            if(collapser.handleTap(x-w/4,y-(h/2-(h/10)*(n+1)/2))) {
                startcb()
            }
        }
    }
    class StackBarCollapserState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale = Math.sin(deg*Math.PI/180).toFloat()
            deg += 10f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    data class StackBarCollapserAnimator(var stackBarsCollapser:StackBarsCollapser,var view:StackBarsCollapserView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            stackBarsCollapser.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                stackBarsCollapser.update({
                    animated = false
                    view.clickListener?.onClick?.invoke()
                })
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                stackBarsCollapser.handleTap(x,y,{
                    animated = true
                    view.postInvalidate()
                })
            }
        }
    }
    data class StackBarCollapserRenderer(var view:StackBarsCollapserView,var time:Int = 0) {
        var animator:StackBarCollapserAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.width.toFloat()
                animator = StackBarCollapserAnimator(StackBarsCollapser(w,h,view.n),view)
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
        fun create(activity:Activity):StackBarsCollapserView {
            val view = StackBarsCollapserView(activity)
            activity.setContentView(view)
            return view 
        }
    }
    data class StackBarClickListener(var onClick:()->Unit)
}