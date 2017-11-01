package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 01/11/17.
 */
class BarOverView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = BarOverRenderer(this)
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
    data class BarOver(var x:Float,var y:Float,var w:Float,var h:Float) {
        val state = BarOverState()
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..1) {
                canvas.save()
                canvas.translate(x+(w/2*(i*2-1))*state.scale,y)
                paint.color = Color.parseColor("#1565C0")
                canvas.drawRoundRect(RectF(-w/2,-h/2,w/2,h/2),w/10,w/10,paint)
                canvas.restore()
            }
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class BarOverState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class BarOverAnimator(var barOver:BarOver,var view:BarOverView) {
        var animated = false
        fun update() {
            if(animated) {
                barOver.update()
                if(barOver.stopped()) {
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
            barOver.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class BarOverRenderer(var view:BarOverView,var time:Int = 0) {
        var animator:BarOverAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = BarOverAnimator(BarOver(w/2,h/2,w/2,w/2),view)
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
        var view:BarOverView?=null
        fun create(activity:Activity) {
            view = BarOverView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}