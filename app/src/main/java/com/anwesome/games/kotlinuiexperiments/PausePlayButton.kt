package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 26/07/17.
 */
class PausePlayButton(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    data class PlayPause(var x:Float,var y:Float,var size:Float,var state:Int = 0) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            if(scale >= 1.0f) {
                state = 1
            }
            if(scale <= 0.0f) {
                state = -1
            }
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            paint.color = Color.parseColor("#1565C0")
            paint.strokeWidth = size/40
            canvas.drawArc(RectF(-size/2,-size/2,size/2,size/2),0.0f,360.0f*scale,false,paint)
            when(state) {
                0 -> {
                    drawPause(canvas,paint,size)
                }
                1 -> {
                    drawPlay(canvas,paint,size)
                }
            }
            canvas.restore()

        }
        private fun drawPlay(canvas:Canvas,paint:Paint,size:Float) {
            paint.style = Paint.Style.FILL
            var path:Path = Path()
            path.moveTo(-size/10,-size/10)
            path.lineTo(size/10,0.0f)
            path.lineTo(-size/10,size/10)
            path.lineTo(-size/10,-size/10)
            canvas.drawPath(path,paint)
        }
        private fun drawPause(canvas:Canvas,paint:Paint,size:Float) {
            paint.strokeWidth = size/25
            var a = size/10
            for(i in 0..1) {
                canvas.drawLine(a*(1-2*i),-a,a*(1-2*i),a,paint)
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/10 && x<=this.x+size/10 && y>=this.y-size/10 && y<=this.y+size/10
    }
    class PPBRenderer{
        var time = 0
        var handler:PPBAnimationHandler?=null
        fun render(canvas:Canvas,paint:Paint,v:PausePlayButton) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var size = 2*Math.min(w,h)/3
                handler = PPBAnimationHandler(PlayPause(w/2,h/2,size),v)
            }
            handler?.draw(canvas,paint)
            handler?.animate()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            handler?.handleTap(x,y)
        }
    }
    class PPBAnimationHandler(var playPause: PlayPause,var v:PausePlayButton,var stateContainer:PPBStateContainer = PPBStateContainer()) {
        var animated = false
        fun draw(canvas: Canvas,paint:Paint) {
            playPause.draw(canvas,paint,1.0f)
        }
        fun animate() {
            if(animated) {
                stateContainer.update()
                if(stateContainer.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && playPause.handleTap(x,y)) {
                stateContainer.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class PPBStateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += 0.2f*dir
            if(scale > 1 || scale < 0) {
                dir = 0
                scale = 0.0f
                if(scale > 1) {
                    scale = 1.0f
                }
            }
        }
        fun startUpdating() {
            dir = when(scale) {
                1.0f -> -1
                0.0f -> 1
                else -> dir
            }
        }
        fun stopped():Boolean = dir == 0
    }
}