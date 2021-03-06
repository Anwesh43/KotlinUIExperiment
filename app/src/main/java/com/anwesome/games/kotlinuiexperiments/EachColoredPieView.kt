package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 27/11/17.
 */

import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.*

val pieAvailableColors:Array<String> = arrayOf("#f44336","#FFC107","#1565C0","#880E4F","#FF5722","#00E676","#3F51B5","#673AB7")
val defaultPieColorSize = 8
class EachColoredPieView(ctx:Context,var n:Int= defaultPieColorSize):View(ctx) {
    var onScaleUpDownListener:EachColoredPieView.OnColorScaleUpDownListener?=null
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = EachColoredPieRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    fun addScaleUpDownListener(scaleUpListener: () -> Unit,scaleDwnListener: () -> Unit) {
        onScaleUpDownListener = OnColorScaleUpDownListener(scaleUpListener,scaleDwnListener)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class EachColoredPie(var i:Int,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float,deg:Float) {
            canvas.save()
            paint.color = Color.parseColor(pieAvailableColors[i])
            canvas.drawArc(RectF(-r,-r,r,r),deg*i,deg*scale,true,paint)
            canvas.restore()
        }
    }
    data class EachColoredPieContainer(var w:Float,var h:Float,var n:Int,val r:Float = Math.min(w,h)/3) {
        var pies:ConcurrentLinkedQueue<EachColoredPie> = ConcurrentLinkedQueue()
        var state = EachColoredPieState()
        init {
            for(i in 0..n) {
                pies.add(EachColoredPie(i,r))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            val deg = 360f/n
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.WHITE
            paint.strokeWidth = r/15
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            pies.forEach { pie ->
                pie.draw(canvas,paint,state.scale,deg)
            }
            canvas.restore()
        }
        fun update(stopcb:()->Unit) {
            state.update()
            if(state.stopped()) {
                stopcb()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating()
            startcb()
        }
    }
    data class EachColoredPieState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                prevScale = scale
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*this.scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class EachColoredPieAnimator(var container:EachColoredPieContainer,var view:EachColoredPieView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{
                    animated = false
                    when(container.state.scale) {
                        0f -> view.onScaleUpDownListener?.scaleDwnListener?.invoke()
                        1f -> view.onScaleUpDownListener?.scaleUpListener?.invoke()
                    }
                }
                try {
                    view.invalidate()
                    Thread.sleep(50)
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun handleTap() {
            container.startUpdating {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class EachColoredPieRenderer(var view:EachColoredPieView,var time:Int = 0) {
        var animator:EachColoredPieAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                animator = EachColoredPieAnimator(EachColoredPieContainer(canvas.width.toFloat(),canvas.height.toFloat(),view.n),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object{
        fun create(activity:MainActivity,vararg n:Int):EachColoredPieView {
            val view = EachColoredPieView(activity)
            if(n.size == 1 && n[0] <= defaultPieColorSize && n[0] > 0) {
                view.n = n[0]
            }
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
            return view
        }
    }
    data class OnColorScaleUpDownListener(var scaleUpListener:()->Unit,var scaleDwnListener:()->Unit)
}