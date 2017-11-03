package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 03/11/17.
 */
import android.app.Activity
import android.content.*
import android.view.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class RingBallView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = RingCenterBallRenderer(this)
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
    data class CenterCornerBall(var x:Float,var y:Float,var r:Float,var size:Float) {
        var state = CenterCornerBallState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(state.getCurrDeg())
            canvas.drawCircle(0f,-size*state.scale,r,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating(deg:Float) {
            state.startUpdating(deg)
        }
        fun stopped():Boolean = state.stopped()
    }
    data class CenterCornerBallState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        private var degs:ConcurrentLinkedQueue<Float> = ConcurrentLinkedQueue()
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                scale = (prevScale+1)%2
                prevScale = scale
                if(dir == 1f) {
                    dir = 0f
                }
                else {
                    dir = 1f
                    degs.remove(degs.getAt(0))
                }
            }
        }
        fun getCurrDeg():Float = degs.getAt(0)?:0f
        fun stopped():Boolean = dir == 0f
        fun startUpdating(deg:Float) {
            degs.add(deg)
            dir = 1-2*this.scale
        }
    }
    data class RingForCenterBall(var deg:Float,var cx:Float,var cy:Float,var r:Float,var x:Float = cx.xInCircle(r,deg),var y:Float = cy.yInCircle(r,deg)) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawCircle(x,y,r/15,paint)
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r/10 && x<=this.x+r/10 && y>=this.y-r/10 && y<=this.y+r/10
    }
    data class RingForCenterBallContainer(var w:Float,var h:Float,var size:Float = Math.min(w,h)/3) {
        var rings: ConcurrentLinkedQueue<RingForCenterBall> = ConcurrentLinkedQueue()
        var centerCornerBall = RingBallView.CenterCornerBall(w / 2, h / 2, size / 10, size)

        init {
            for (i in 0..3) {
                rings.add(RingForCenterBall(90 * i + 45f, w / 2, h / 2, size))
            }
        }

        fun draw(canvas: Canvas, paint: Paint) {
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(w / 2, h / 2, size / 10, paint)
            rings.forEach { ring ->
                ring.draw(canvas, paint)
            }
            paint.style = Paint.Style.FILL
            centerCornerBall.draw(canvas, paint)
        }

        fun update() {
            centerCornerBall.update()
        }

        fun handleTap(x: Float, y: Float,startcb:()->Unit) {
            rings.forEach { ring ->
                if (ring.handleTap(x, y)) {
                    centerCornerBall.startUpdating(ring.deg)
                    startcb()
                    return
                }
            }
        }

        fun stopped(): Boolean = centerCornerBall.stopped()
    }
    class RingCenterCornerBallAnimator(var container:RingForCenterBallContainer,var view:RingBallView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update()
                if(container.stopped()) {
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
    class RingCenterBallRenderer(var view:RingBallView,var time:Int = 0) {
        var animator:RingCenterCornerBallAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                paint.color = Color.parseColor("#FF9800")
                paint.strokeWidth = Math.min(w,h)/40
                animator = RingCenterCornerBallAnimator(RingForCenterBallContainer(w,h),view)
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
        var view:RingBallView?=null
        fun create(activity:Activity) {
            view = RingBallView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}
fun ConcurrentLinkedQueue<Float>.getAt(i:Int):Float? {
    var index = 0
    this.forEach {
        if(i == index) {
            return it
        }
        index++
    }
    return null
}

fun Float.xInCircle(r:Float,deg:Float) = this+r*Math.cos(deg*Math.PI/180).toFloat()
fun Float.yInCircle(r:Float,deg:Float) = this+r*Math.sin(deg*Math.PI/180).toFloat()