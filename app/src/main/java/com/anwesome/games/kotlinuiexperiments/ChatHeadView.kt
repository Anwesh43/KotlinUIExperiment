package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 05/09/17.
 */
class ChatHeadView(ctx:Context):View(ctx) {
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
    data class ChatHead(var size:Float,var state:ChatHeadState = ChatHeadState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(4*size/5,4*size/5)
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,size/10,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i+45f*this.state.scale)
                paint.strokeWidth = size/40
                canvas.drawLine(0f,-size/30,0f,size/30,paint)
                canvas.restore()
            }
            canvas.restore()
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
        fun render(canvas:Canvas,paint: Paint) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

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
}