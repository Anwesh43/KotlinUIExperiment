package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 15/10/17.
 */

class CrossTapView(context:Context):SurfaceView(context) {
    var runner = UIRunner(view=this)
    var thread = Thread(runner)
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
        if(holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawColor(Color.parseColor("#212121"))
            holder.unlockCanvasAndPost(canvas)
        }
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
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
        fun draw(canvas:Canvas) {

        }
        fun update() {

        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    data class CrossTapCircle(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,size/2,paint)
            canvas.save()
            canvas.rotate(45f)
            canvas.drawCross(0f,0f,2*size/3,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = true
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