package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 05/09/17.
 */
class ChatHeadView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer:ChatHeadRenderer = ChatHeadRenderer()
    override fun onDraw(canvas:Canvas) {
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
    data class ChatHead(var size:Float,var state:ChatHeadState = ChatHeadState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(4*size/5,4*size/5)
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,size/10,paint)
            paint.color = Color.WHITE
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i+45f*this.state.scale)
                paint.strokeWidth = size/40
                canvas.drawLine(0f,-size/30,0f,size/30,paint)
                canvas.restore()
            }
            canvas.restore()
            paint.color = Color.parseColor("#1565C0")
            canvas.save()
            canvas.translate(size/10,size/10)
            var clipPath = Path()
            clipPath.addRect(RectF(0f,0.6f*size*(1-this.state.scale),0.6f*size,0.6f*size),Path.Direction.CCW)
            canvas.clipPath(clipPath)
            canvas.drawRoundRect(RectF(0f,0f,size*0.6f,size*0.5f),size/10,size/10,paint)
            var path = Path()
            path.moveTo(size*0.5f,size*0.5f)
            path.lineTo(size*0.5f,size*0.6f)
            path.lineTo(size*0.45f,size*0.5f)
            canvas.drawPath(path,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean {
            return x>=0.7f*size && x<=0.9f*size && y>=0.7f*size && y<=0.9f*size
        }
        fun update() {
            this.state.update()
        }
        fun startUpdating() {
            this.state.startUpdating()
        }
        fun stopped():Boolean = this.state.stopped()
    }
    data class ChatHeadState(var scale:Float = 0f,var dir:Float=0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
    class ChatHeadRenderer {
        var time = 0
        var chatHeadAnimator:ChatHeadAnimator?=null
        fun render(canvas:Canvas,paint: Paint,view:ChatHeadView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                chatHeadAnimator = ChatHeadAnimator(ChatHead(Math.min(w,h)),view)
            }
            chatHeadAnimator?.draw(canvas,paint)
            chatHeadAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            chatHeadAnimator?.handleTap(x,y)
        }
    }
    class ChatHeadAnimator(var chatHead:ChatHead,var view:ChatHeadView) {
        var animated = false
        fun update() {
            if(animated) {
                chatHead.update()
                if(chatHead.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            chatHead.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(chatHead.handleTap(x,y)) {
                animated = true
                chatHead.startUpdating()
                view.postInvalidate()
            }
        }

    }
    companion object {
        fun create(activity:Activity) {
            var size = DimensionsUtil.getDimension(activity)
            var view = ChatHeadView(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}