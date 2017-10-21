package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.graphics.*
import android.content.Context
/**
 * Created by anweshmishra on 21/10/17.
 */
class FourCornerBallView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class FourCornerBall(var w:Float,var h:Float) {
        val state = FourCornerBallState()
        fun draw(canvas:Canvas,paint:Paint) {
            for(i in 0..3) {
                canvas.save()
                canvas.translate(w/2,h/2)
                canvas.drawCircle(0f-((w/3)+(2*w/3)*(i%2)),0f-((w/3+(2*w/3)*(i/2))),Math.min(w,h)/10,paint)
                canvas.restore()
            }
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
    }
}