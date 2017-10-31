package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 31/10/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
class CrossFillerView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = CrossFillerRenderer(this)
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
    data class CrossFiller(var x:Float,var y:Float,var size:Float) {
        var state = CrossFillerState()
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45f*state.scales[1])
            paint.strokeWidth = size/30
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(i*90f)
                paint.color = Color.GRAY
                canvas.drawLine(0f,0f,0f,-size/2,paint)
                paint.color = Color.BLUE
                canvas.drawLine(0f,-size/2+size/2*state.scales[0],0f,-size/2,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class CrossFillerState(var dir:Int = 0,var j:Int = 0,var prevScale:Float = 0f,var currDir:Int = 1) {
        var scales:Array<Float> = arrayOf(0f,0f)
        fun update() {
            scales[j] += dir*0.1f
            if(Math.abs(scales[j]-prevScale) > 1) {
                scales[j] = (prevScale+1)%2
                prevScale = scales[j]
                if(j == scales.size || j == -1) {
                    currDir *= -1
                    j+=currDir
                    dir = 0
                }
                else {
                    j+=dir
                }
            }
        }
        fun startUpdating() {
            dir = currDir
        }
        fun stopped():Boolean = dir == 0
    }
    class CrossFillerAnimator(var crossFiller:CrossFiller,var view:CrossFillerView) {
        var animated = false
        fun update() {
            if(animated) {
                crossFiller.update()
                if(crossFiller.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            crossFiller.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                crossFiller.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class CrossFillerRenderer(var view:CrossFillerView,var time:Int = 0) {
        var animator:CrossFillerAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = CrossFillerAnimator(CrossFiller(w/2,h/2,2*Math.min(w,h)/3),view)
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
        fun create(activity:Activity) {
            val view = CrossFillerView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}