package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 26/07/17.
 */
class PausePlayButton(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = PPBRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class PlayPause(var x:Float,var y:Float,var size:Float,var state:Int = 0) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            if(scale >= 1.0f) {
                state = 1
            }
            if(scale <= 0.0f) {
                state = 0
            }
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            paint.color = Color.parseColor("#1565C0")
            paint.strokeWidth = size/25
            canvas.drawArc(RectF(-size/2,-size/2,size/2,size/2),0.0f,360.0f*scale,false,paint)
            paint.color = Color.WHITE
            paint.strokeWidth = size/15
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
            paint.style = Paint.Style.FILL_AND_STROKE
            var path:Path = Path()
            path.moveTo(-size/10,-size/10)
            path.lineTo(size/10,0.0f)
            path.lineTo(-size/10,size/10)
            path.lineTo(-size/10,-size/10)
            canvas.drawPath(path,paint)
        }
        private fun drawPause(canvas:Canvas,paint:Paint,size:Float) {
            var a = size/10
            for(i in 0..1) {
                canvas.drawLine(a*(1-2*i),-a,a*(1-2*i),a,paint)
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/5 && x<=this.x+size/5 && y>=this.y-size/5 && y<=this.y+size/5
    }
    class PPBRenderer{
        var time = 0
        var handler:PPBAnimationHandler?=null
        fun render(canvas:Canvas,paint:Paint,v:PausePlayButton) {
            if(time == 0) {
                paint.strokeCap = Paint.Cap.ROUND
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
            playPause.draw(canvas,paint,stateContainer.scale)
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
                if(scale > 1) {
                    scale = 1.0f
                }
                else if(scale < 0.0f){
                    scale = 0.0f
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
    companion object {
        fun create(activity: Activity) {
            var size:Point = DimensionsUtil.getDimension(activity)
            activity.addContentView(PausePlayButton(activity), ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
}