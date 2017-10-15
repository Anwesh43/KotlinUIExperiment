package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.*
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
    class CrossTapRenderer(var w:Float,var h:Float){
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
        fun create(x:Float,y:Float,size:Float) {
            crossTaps.add(CrossTapCircle(x,y,size))
        }
        fun handleTap(x:Float,y:Float) {
            crossTaps.forEach { crossTap ->
                if(crossTap.handleTap(x,y)) {
                    crossTap.startUpdating()
                    updatingTaps.add(crossTap)
                    return
                }

            }
        }
    }
    data class CrossTapCircle(var x:Float,var y:Float,var size:Float,var state:CrossTapState = CrossTapState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = size/10
            paint.color = Color.parseColor("#0097A7")
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawArc(RectF(-size/2,-size/2,size/2,size/2),0f,360f*state.scales[2],false,paint)
            canvas.save()
            canvas.rotate(45f*state.scales[0])
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
        var scales:Array<Float> = arrayOf(1f,1f,1f)
        fun update() {
            if(j < scales.size) {
                scales[j] -= dir*0.2f
                if(scales[j] < 0) {
                    scales[j] = 0f
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
        var time = 1
        fun render() {
            if(holder.surface.isValid) {
                val canvas = holder.lockCanvas()
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                if(time == 1) {
                    renderer = CrossTapRenderer(w,h)
                }
                canvas.drawColor(Color.parseColor("#212121"))
                renderer?.draw(canvas)
                renderer?.update()
                if(time % 20 == 0) {
                    val random = Random()
                    val size = Math.min(w,h)/10
                    val x = size+random.nextInt((w-size).toInt()).toFloat()
                    val y = size+random.nextInt((h-size).toInt()).toFloat()
                    renderer?.create(x,y,size)
                }
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