package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 15/10/17.
 */

class CrossTapView(context:Context):SurfaceView(context) {
    fun pause() {

    }
    fun resume() {

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
    class GameRunner(var animated:Boolean = true,var view:CrossTapView):Runnable {
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
        fun pause() {
            animated = false
        }
        fun resume() {
            animated = true
        }
    }
}