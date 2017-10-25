package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 25/10/17.
 */
class RectEdgeView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RectEdgeRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                renderer.handleTap(event.x,event.y)
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
            canvas.drawCircle(0f,0f,r/5,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.drawCircle(0f,0f,(r/5)*state.scale,paint)
            canvas.restore()
            canvas.save()
            canvas.rotate(90f*i+90*state.scale)
            canvas.drawLine(0f,0f,0f,r*Math.sqrt(2.0).toFloat(),paint)
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
        fun handleTap(x:Float,y:Float):Boolean = x >= this.x - r/5 && x<=this.x+r/5 && y>=this.y-r/5 && y<=this.y+r/5
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
    data class RectEdgeContainer(var w:Float,var h:Float) {
        var rectEdges:ConcurrentLinkedQueue<RectEdge> = ConcurrentLinkedQueue()
        var updatingEdges:ConcurrentLinkedQueue<RectEdge> = ConcurrentLinkedQueue()
        init {
            for(i in 0..3) {
                rectEdges.add(RectEdge(i,Math.min(w,h)/4))
            }
        }
        fun update(stopcb:()->Unit) {
            updatingEdges.forEach { updatingEdge ->
                updatingEdge.update()
                if(updatingEdge.stopped()) {
                    updatingEdges.remove(updatingEdge)
                    if(updatingEdges.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            rectEdges.forEach { rectEdge ->
                rectEdge.draw(canvas,paint)
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            rectEdges.forEach { rectEdge ->
                if(rectEdge.handleTap(x-w/2,y-h/2)) {
                    rectEdge.startUpdating()
                    updatingEdges.add(rectEdge)
                    if(updatingEdges.size == 1) {
                        startcb()
                    }
                }
            }
        }
    }
    class RectEdgeAnimator(var container:RectEdgeContainer,var view:RectEdgeView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                container.update({
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
            container.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            container.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
    }
    class RectEdgeRenderer(var view:RectEdgeView,var time:Int = 0) {
        var animator:RectEdgeAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = RectEdgeAnimator(RectEdgeContainer(w,h),view)
                paint.strokeWidth = Math.min(w,h)/50
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = Color.parseColor("#FBC02D")
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
        var view:RectEdgeView?=null
        fun create(activity:Activity) {
            view = RectEdgeView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}