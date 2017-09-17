package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 17/09/17.
 */

class PlayBarView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = PlayBarRenderer()
    var listener:PlayBarListener?=null
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
    data class PlayBar(var w:Float,var h:Float,var state:PlayBarState = PlayBarState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            val color = Color.parseColor("#d32f2f")
            canvas.save()
            canvas.translate(w/2,h/2)
            canvas.save()
            var clipPath = Path()
            clipPath.addRect(RectF(-w/10,-h/10,-w/10+(w/5)*state.scale,h/10),Path.Direction.CW)
            canvas.clipPath(clipPath)
            drawTriangle(canvas,paint,color,true)
            drawTriangle(canvas,paint,color,false)
            canvas.restore()
            canvas.save()
            var newClipPath = Path()
            newClipPath.addRect(RectF(-w/10+w/5*state.scale,-h/10,w/10,h/10),Path.Direction.CW)
            canvas.clipPath(newClipPath)
            drawTriangle(canvas,paint,Color.WHITE,false)
            canvas.restore()
            paint.style = Paint.Style.STROKE
            drawCircle(canvas,paint,1f,Color.WHITE)
            drawCircle(canvas,paint,state.scale,color)
            canvas.restore()
            drawLine(canvas,paint,1f,Color.WHITE)
            drawLine(canvas,paint,state.scale,color)
        }
        private fun drawTriangle(canvas: Canvas,paint:Paint,color:Int,fill:Boolean) {
            if(fill) {
                paint.style = Paint.Style.FILL
            }
            else {
                paint.style = Paint.Style.STROKE
            }
            val path = Path()
            path.moveTo(-w/11,-h/11)
            path.lineTo(w/11,0f)
            path.lineTo(-w/11,h/11)
            path.lineTo(-w/11,-h/11)
            paint.color = color
            canvas.drawPath(path,paint)
        }
        fun handleTap(x:Float,y:Float) = x >= w/2 - w/10 && x <= w/2 + w/10 && y >= h/2 - h/10 && y <= h/2 + h/10
        private fun drawLine(canvas: Canvas,paint:Paint,scale:Float,color:Int) {
            paint.color = color
            canvas.drawLine(0f,4*h/5,w*scale,4*h/5,paint)
        }
        private fun drawCircle(canvas:Canvas,paint:Paint,scale:Float,color:Int) {
            var circlePath = Path()
            val finalDeg = (360*scale).toInt()
            for(i in 0..finalDeg) {
                val cx = ((w/5)*Math.cos((i-90)*Math.PI/180)).toFloat()
                val cy = ((w/5)*Math.sin((i-90)*Math.PI/180)).toFloat()
                if(i == 0) {
                    circlePath.moveTo(cx,cy)
                }
                circlePath.lineTo(cx,cy)
            }
            paint.color = color
            canvas.drawPath(circlePath,paint)
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
    }
    data class PlayBarState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class PlayBarAnimator(var playBar:PlayBar,var view:PlayBarView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                playBar.update()
                if(playBar.stopped()) {
                    animated = false
                    when(playBar.state.scale) {
                        0f -> view.listener?.emptyListener?.invoke()
                        1f -> view.listener?.fillListener?.invoke()
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            playBar.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(playBar.handleTap(x,y) && !animated) {
                animated = true
                playBar.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class PlayBarRenderer {
        var time = 0
        var playBarAnimator:PlayBarAnimator? = null
        fun render(canvas: Canvas,paint:Paint,view:PlayBarView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                paint.strokeWidth = Math.min(w,h)/50
                paint.strokeCap = Paint.Cap.ROUND
                val playBar = PlayBar(w,h)
                playBarAnimator = PlayBarAnimator(playBar,view)
            }
            playBarAnimator?.draw(canvas,paint)
            playBarAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            playBarAnimator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity,vararg listeners:()->Unit) {
            var view = PlayBarView(activity)
            var size = DimensionsUtil.getDimension(activity)
            if(listeners.size == 2) {
                view.listener = PlayBarListener(listeners[0],listeners[1])
            }
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
    data class PlayBarListener(var fillListener:()->Unit,var emptyListener:()->Unit)
}