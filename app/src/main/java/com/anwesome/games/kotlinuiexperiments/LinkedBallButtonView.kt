package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 18/10/17.
 */

class LinkedBallButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer = LinkedBallButtonRenderer(this)
    var selectionListener:LinkedBallButtonSelectionListener?=null
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
    data class BallButton(var x:Float,var y:Float,var r:Float,var size:Float) {
        var state:BallButtonState = BallButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#f44336")
            paint.strokeWidth = r/10
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*state.scale,true,paint)
            canvas.drawLine(r,0f,r+size*state.scale,0f,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y -r && y<=this.y+r
    }
    data class BallButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1 ){
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir==0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
    data class LinkedBallButton(var w:Float,var h:Float) {
        var holder:UpdatingBallButtonHolder?=null
        var ballButtons:ConcurrentLinkedQueue<BallButton> = ConcurrentLinkedQueue()
        init {
            val n = 6
            if(w>h) {
                val r = h / 4
                val size = (w - n * 2*r) / n
                var x = r
                for (i in 1..n) {
                    ballButtons.add(BallButton(x,h/2,r,size))
                    x += 2*r+size
                }
                holder = UpdatingBallButtonHolder(ballButtons)
            }
        }
        fun update(stopcb:()->Unit,listener:LinkedBallButtonSelectionListener?) {
            holder?.update(listener)
            if(holder?.stopped()?:false) {
                stopcb()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            ballButtons.forEach{ ballButton ->
                ballButton.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            var j = 0
            ballButtons.forEach { ballButton ->
                if(ballButton.handleTap(x,y)) {
                    holder?.startUpdating(j)
                    startcb()
                }
                j++
            }
        }
    }
    data class UpdatingBallButtonHolder(var ballButtons:ConcurrentLinkedQueue<BallButton>,var curr:Int=0,var till:Int = 0) {
        var updatingBalls:LinkedList<BallButton> = LinkedList()
        var prevDir = 0
        var dir:Int = 0
        init {
            ballButtons.forEach {
                updatingBalls.add(it)
            }

        }
        fun startUpdating(j:Int) {
            dir = 1 - 2* updatingBalls[j].state.scale.toInt()
            if(prevDir == dir) {
                curr += dir
            }
            updatingBalls[curr].startUpdating()
            till = j

        }
        fun update(listener:LinkedBallButtonSelectionListener?) {
            updatingBalls[curr].update()
            if(updatingBalls[curr].stopped()) {
                when(dir) {
                    -1 -> listener?.collapseListener?.invoke(curr)
                    1 -> listener?.expandListener?.invoke(curr)
                }
                if(curr == till) {
                    prevDir = dir
                    dir = 0
                }
                else {
                    curr += dir
                    updatingBalls[curr].startUpdating()
                }
            }
        }
        fun stopped():Boolean = dir == 0
    }
    class LinkedBallButtonAnimator(var linkedBallButton:LinkedBallButton,var view:LinkedBallButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                linkedBallButton.update({
                    animated = false
                },view.selectionListener)
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            linkedBallButton.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            linkedBallButton.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
    }
    class LinkedBallButtonRenderer(var view:LinkedBallButtonView,var time:Int = 0) {
        var linkedBallAnimator:LinkedBallButtonAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                linkedBallAnimator = LinkedBallButtonAnimator(LinkedBallButton(w,h),view)
            }
            linkedBallAnimator?.draw(canvas,paint)
            linkedBallAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            linkedBallAnimator?.handleTap(x,y)
        }
    }
    companion object {
        var view:LinkedBallButtonView?=null
        fun create(activity:Activity) {
            view = LinkedBallButtonView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y/8))
        }
        fun addSelectionListener(vararg listeners:(Int)->Unit) {
            view?.selectionListener = LinkedBallButtonSelectionListener(listeners[0],listeners[1])
        }
    }
    data class LinkedBallButtonSelectionListener(var expandListener:(Int)->Unit,var collapseListener:(Int)->Unit)
}