package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.graphics.*
import android.content.Context
/**
 * Created by anweshmishra on 21/10/17.
 */
class FourCornerBallView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var onClickListener:OnFourCornerBallClickListener?=null
    val renderer = FourBallRenderer(view = this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class FourCornerBall(var w:Float,var h:Float) {
        val state = FourCornerBallState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#673AB7")
            canvas.save()
            canvas.translate(w/2,h/2)
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f*i+45f)
                canvas.translate(0f,-0.4f*w*state.scale)
                canvas.drawCircle(0f,0f,Math.min(w,h)/10,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb:()->Unit) {
            state.update()
            if(state.stopped()) {
                stopcb()
            }
        }
    }
    data class FourCornerBallState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            deg += 4.5f
            scale = Math.sin(deg*Math.PI/180).toFloat()
            if(deg > 180f) {
                deg = 0f
                scale = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class FourCornerBallAnimator(var fourCornerBall:FourCornerBall,var view:FourCornerBallView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                fourCornerBall.update({
                    animated = false
                    view.onClickListener?.listener?.invoke()
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
            fourCornerBall.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class FourBallRenderer(var time:Int = 0,var view:FourCornerBallView) {
        var animator:FourCornerBallAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = FourCornerBallAnimator(FourCornerBall(w,h),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        var view:FourCornerBallView?=null
        fun create(activity:Activity) {
            view = FourCornerBallView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
        fun addListener(vararg listeners:()->Unit) {
            if(listeners.size == 1) {
                view?.onClickListener = OnFourCornerBallClickListener(listeners[0])
            }
        }
    }
    data class OnFourCornerBallClickListener(var listener:()->Unit)
}