package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.graphics.*
import android.view.*
import android.content.Context
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 17/10/17.
 */
class SideWiseLineView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = SideWiseLineRenderer(this)
    var sideWiseExpandListener:OnSideWiseLineExpandListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#ffebee"))
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
    data class SideWiseLine(var i:Int,var w:Float,var h:Float,var cx:Float = (i%2)*w,var cy:Float = (h/20),var cr:Float = (h/25)) {
        var state:SideWiseLineState = SideWiseLineState()
        fun draw(canvas:Canvas,paint:Paint) {
            val x = cx+cr*(1-2*i)
            val diffX = (w/2-x)
            canvas.save()
            canvas.translate(x,cy)
            canvas.rotate(45f*state.scale)
            paint.color = Color.parseColor("#3F51B5")
            canvas.drawCircle(0f,0f,cr,paint)
            paint.color = Color.parseColor("#FAFAFA")
            for(j in 0..1) {
                canvas.save()
                canvas.rotate(90f*j)
                canvas.drawLine(0f,-(cr)/2,0f,(cr)/2,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.save()
            canvas.translate(x,h/10)
            var y = 0f
            paint.color = Color.parseColor("#673AB7")
            for(j in 0..9) {
                canvas.drawLine(0f,y,diffX*state.scale,y,paint)
                y+=(0.9f*h)/10
            }
            canvas.restore()
        }
        fun update(stopCb:()->Unit) {
            state.update()
            if(state.stopped()) {
                stopCb()
            }
        }
        fun handleTap(x:Float,y:Float,startCb:()->Unit) {
            if(x>=cx-2*cr*(i%2) && x<=cx+2*cr*((i+1)%2) && y>=cy-cr && y<=cy+cr && state.dir == 0f) {
                state.startUpdating()
                startCb()
            }
        }
    }
    data class SideWiseLineState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update(){
            scale += 0.1f*dir
            if(scale > 1 ){
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class SideWiseLineContainer(var w:Float,var h:Float) {
        var lines:ConcurrentLinkedQueue<SideWiseLine> = ConcurrentLinkedQueue()
        init {
            for(i in 0..1) {
                lines.add(SideWiseLine(i,w,h))
            }
        }
        var tappedLines:ConcurrentLinkedQueue<SideWiseLine> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas,paint:Paint) {
            lines.forEach{ line ->
                line.draw(canvas,paint)
            }
        }
        fun update(stopcb:()->Unit,listener:OnSideWiseLineExpandListener?) {
            tappedLines.forEach { line ->
                line.update({
                    tappedLines.remove(line)
                    if(tappedLines.size == 0) {
                        stopcb()
                    }
                    when(line.state.scale) {
                        0f -> listener?.collapseListener?.invoke(line.i)
                        1f -> listener?.expandListener?.invoke(line.i)
                    }
                })
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            var tapped = false
            lines.forEach { line ->
                if(tapped) {
                    return
                }
                line.handleTap(x,y,{
                    tappedLines.add(line)
                    tapped = true
                    if(tappedLines.size == 1) {
                        startcb()
                    }
                })

            }
        }
    }
    class SideWiseLineAnimator(var container:SideWiseLineContainer,var view:SideWiseLineView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                container.update({
                    animated = false
                },view.sideWiseExpandListener)
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
    class SideWiseLineRenderer(var view:SideWiseLineView,var time:Int = 0) {
        var animator:SideWiseLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = SideWiseLineAnimator(SideWiseLineContainer(w,h),view)
                paint.strokeWidth = Math.min(w,h)/50
                paint.strokeCap = Paint.Cap.ROUND
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
        var view:SideWiseLineView?=null
        fun create(activity:Activity) {
            view = SideWiseLineView(activity)
            activity.setContentView(view)
        }
        fun addListener(vararg listeners:(Int)->Unit) {
            if(listeners.size == 2) {
                view?.sideWiseExpandListener = OnSideWiseLineExpandListener(listeners[0],listeners[1])
            }
        }
    }
    data class OnSideWiseLineExpandListener(var expandListener:(Int)->Unit,var collapseListener:(Int)->Unit)
}