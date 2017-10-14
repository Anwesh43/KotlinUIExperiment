package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 14/10/17.
 */
class CenterToCornerBallView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CCBRenderer(this)
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
    data class CornerBall(var i:Int,var w:Float,var h:Float,var r:Float,var x:Float = 0f,var y:Float = 0f) {
        init {
            x = (i%2)*(w-2*r)+r
            y = (i/2)*(h-2*r)+r
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = r/10
            paint.color = Color.parseColor("#FF9800")
            canvas.save()
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(x,y,r,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CenterBall(var r:Float,var x:Float=0f,var y:Float=0f,var wx:Float = 0f,var wy:Float = 0f) {
        var state:CenterToBallState = CenterToBallState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#1A237E")
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(state.scales[0],state.scales[0])
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*(1-state.scales[2]),true,paint)
            canvas.restore()
        }
        fun update(){
            x+=wx*state.scales[1]
            y+=wy*state.scales[1]
        }
        fun setDiff(x:Float,y:Float) {
            if(state.j == 1 && state.dir == 0f) {
                wx = x
                wy = y
                state.startUpdating()
            }
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun reachedFinalState():Boolean = state.reachedFinalState()
    }
    data class CenterToBallState(var dir:Float = 0f,var j:Int = 0) {
        var scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update() {
            if(j < scales.size) {
                scales[j]+=dir*0.1f
                if(scales[j] > 1) {
                    if(j != 1) {
                        dir = 0f
                    }
                    scales[j] = 1f
                    j++
                }
            }
        }
        fun reachedFinalState():Boolean = dir == 0f && j == scales.size
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
            }
        }
    }
    class CCBController(var w:Float,var h:Float,var view:CenterToCornerBallView,var r:Float = Math.min(w,h)/20) {
        var cornerBalls:LinkedList<CornerBall> = LinkedList()
        var centerBall = CenterBall(r)
        var animated = false
        var ballsInMotion:ConcurrentLinkedQueue<CenterBall> = ConcurrentLinkedQueue()
        init {
            for(i in 0..3) {
                cornerBalls.add(CornerBall(i,w,h,r))
            }
            animated = true
            centerBall.startUpdating()
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            ballsInMotion.forEach { ballInMotion ->
                ballInMotion.draw(canvas,paint)
            }
            centerBall.draw(canvas,paint)
            canvas.restore()
            cornerBalls.forEach { cornerBall ->
                cornerBall.draw(canvas,paint)
            }
        }
        fun update() {
            if(animated) {
                centerBall.update()
                ballsInMotion.forEach { ballInMotion ->
                    ballInMotion.update()
                    if(ballInMotion.stopped()) {
                        ballsInMotion.remove(ballInMotion)
                    }
                }
                if(ballsInMotion.size == 0 && centerBall.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            cornerBalls.forEach { cornerBall ->
                if(cornerBall.handleTap(x,y) && centerBall.state.dir == 0f && centerBall.state.j == 1) {
                    centerBall.setDiff(cornerBall.x-w/2,cornerBall.y-h/2)
                    ballsInMotion.add(centerBall)
                    centerBall = CenterBall(r)
                    centerBall.startUpdating()
                    if(ballsInMotion.size == 1) {
                        animated = true
                        view.postInvalidate()
                    }
                    return
                }
            }
        }
    }
    class CCBRenderer(var view:CenterToCornerBallView) {
        var time:Int = 0
        var controller:CCBController ?= null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                controller = CCBController(w,h,view)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
}