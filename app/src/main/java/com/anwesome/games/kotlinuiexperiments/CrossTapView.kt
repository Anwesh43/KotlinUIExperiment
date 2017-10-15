package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 15/10/17.
 */

class CrossTapView(context:Context):SurfaceView(context) {
    var runner = UIRunner(view=this)
    var thread = Thread(runner)
    var controller = UiController(holder)
    init {
        thread.start()
    }
    fun pause() {
        runner.pause({
            while(true) {
                try {
                    thread.join()
                    break
                }
                catch(ex:Exception) {

                }
            }
        })
    }
    fun resume() {
        runner.resume({
            thread = Thread(runner)
            thread.start()
        })
    }
    fun render() {
        controller.render()
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        controller.handleTap(event)
        return true
    }
    class UIRunner(var animated:Boolean = true,var view:CrossTapView):Runnable {
        override fun run() {
            while(animated) {
                view.render()
                try {
                    Thread.sleep(75)
                }
                catch(ex:Exception) {

                }
            }
        }
        fun pause(cb:()->Unit) {
            if(animated) {
                animated = false
                cb()
            }
        }
        fun resume(cb:()->Unit) {
            if(!animated) {
                animated = true
                cb()
            }
        }
    }
    class CrossTapRenderer(var w:Float,var h:Float,var size:Float = Math.min(w,h)/15){
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var crossTaps:ConcurrentLinkedQueue<CrossTapCircle> = ConcurrentLinkedQueue()
        var updatingTaps:ConcurrentLinkedQueue<CrossTapCircle> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas) {
            crossTaps.forEach { crossTapCircle ->
                crossTapCircle.draw(canvas,paint)
            }
        }
        fun update() {
            updatingTaps.forEach { crossTapCircle ->
                crossTapCircle.update()
                if(crossTapCircle.stopped()) {
                    updatingTaps.remove(crossTapCircle)
                }
            }
        }
        fun create(x:Float,y:Float) {
            crossTaps.add(CrossTapCircle(x,y,size))
        }
        fun handleTap(x:Float,y:Float) {
            crossTaps.forEach { crossTap ->
                crossTap.startUpdating()
                if(crossTap.handleTap(x,y)) {
                    updatingTaps.add(crossTap)
                }
                return
            }
        }
    }
    data class CrossTapCircle(var x:Float,var y:Float,var size:Float,var state:CrossTapState = CrossTapState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawArc(RectF(-size/2,-size/2,size/2,size/2),0f,360f*state.scales[0],false,paint)
            canvas.save()
            canvas.rotate(45f*state.scales[2])
            canvas.drawCross(0f,0f,2*size/3*state.scales[1],paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size && x<=this.x+size && y>=this.y-size && y<=this.y+size
        fun stopped():Boolean = state.stopped()
    }
    data class CrossTapState(var dir:Float = 0f,var j:Int = 0) {
        var scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update() {
            if(j < scales.size) {
                scales[j] += dir*0.1f
                if(scales[j] > 1) {
                    scales[j] = 1f
                    j++
                    if(j == scales.size) {
                        dir = 0f
                    }
                }
            }
        }
        fun stopped():Boolean = dir == 0f && j == scales.size
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
            }
        }
    }
    class UiController(var holder:SurfaceHolder) {
        var renderer:CrossTapRenderer?=null
        var time = 0
        fun render() {
            if(holder.surface.isValid) {
                val canvas = holder.lockCanvas()
                if(time == 0) {
                    renderer = CrossTapRenderer(canvas.width.toFloat(),canvas.height.toFloat())
                }
                canvas.drawColor(Color.parseColor("#212121"))
                renderer?.draw(canvas)
                renderer?.update()
                holder.unlockCanvasAndPost(canvas)
                time++
            }
        }
        fun handleTap(event:MotionEvent) {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    renderer?.handleTap(event.x,event.y)
                }
            }
        }
    }
    companion object {
        fun create(activity:Activity):CrossTapView {
            val view = CrossTapView(activity)
            activity.setContentView(view)
            return view
        }
    }
}
fun Canvas.drawCross(x:Float,y:Float,size:Float,paint:Paint) {
    this.save()
    this.translate(x,y)
    for(i in 0..1) {
        this.save()
        this.rotate(90f*i)
        this.drawLine(0f,-size/2,0f,size/2,paint)
        this.restore()
    }
    this.restore()
}