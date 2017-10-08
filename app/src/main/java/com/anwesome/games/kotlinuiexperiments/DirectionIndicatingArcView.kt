package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.content.Context
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 08/10/17.
 */
class DirectionIndicatingArcView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DIARenderer(this)
    var arrowSelectionListener:DirectionArrowOnSelectionListener? = null
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
    data class DirectionIndicatingArcContainer(var w:Float,var h:Float,var arcs:ConcurrentLinkedQueue<DirectionIndicatingArc> = ConcurrentLinkedQueue(),var state:DIAState = DIAState()) {
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
            curr?.scale = state.scale
            prev?.scale = 1-state.scale
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.drawRotatingTriangle(0f,0f,(prev?.deg?:0f)+gapDeg*state.scale,Math.min(w,h)/10,paint)
            arcs.forEach { arc ->
                arc.draw(canvas,paint)
            }
            canvas.restore()
        }
        fun update(stopcb:()->Unit) {
            state.update()
            if(state.stopped()) {
                prev?.scale = 0f
                prev = curr
                state.scale = 0f
                gapDeg = 0f
                curr = null
                stopcb()
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            arcs.forEach { arc ->
                if(arc.handleTap(x-w/2,y-h/2) && arc != prev) {
                    gapDeg = arc.deg - (prev?.deg?:0f)
                    curr = arc
                    state.startUpdating()
                    startcb()
                    return
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
    class DirectionIndicatingArcAnimator(var container:DirectionIndicatingArcContainer,var view:DirectionIndicatingArcView) {
        var animated:Boolean = false
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update({
                    animated = false
                    val deg = (container.prev?.deg?.toInt()?:0)
                    view.arrowSelectionListener?.listener?.invoke(deg/90)
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
                container.handleTap(x,y,{
                    animated = true
                    view.postInvalidate()
                })
            }
        }
    }
    class DIARenderer(var view:DirectionIndicatingArcView,var time:Int = 0) {
        var animator:DirectionIndicatingArcAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                paint.strokeWidth = Math.min(w,h)/50
                paint.color = Color.parseColor("#FF6F00")
                animator = DirectionIndicatingArcAnimator(DirectionIndicatingArcContainer(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object{
        fun create(activity:Activity,vararg listeners:(Int)->Unit) {
            val view = DirectionIndicatingArcView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.y))
            if(listeners.size == 1) {
                view.arrowSelectionListener = DirectionArrowOnSelectionListener(listeners[0])
            }
        }
    }
    data class DirectionArrowOnSelectionListener(var listener:(Int)->Unit)
}
fun Canvas.drawRotatingTriangle(x:Float,y:Float,deg:Float,size:Float,paint:Paint) {
    this.save()
    this.translate(x,y)
    this.rotate(deg)
    val path = Path()
    path.moveTo(-size/2,-size/2)
    path.lineTo(-size/2,size/2)
    path.lineTo(size/2,0f)
    this.drawPath(path,paint)
    this.restore()
}