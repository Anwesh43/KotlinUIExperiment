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
}