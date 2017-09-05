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
    data class ChatHead(var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(4*size/5,4*size/5)
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,size/10,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i+45f)
                paint.strokeWidth = size/40
                canvas.drawLine(0f,-size/30,0f,size/30,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.save()
            canvas.translate(size/10,size/10)
            var clipPath = Path()
            clipPath.addRect(RectF(0f,0.6f*size*(1),0.6f*size,0.6f*size),Path.Direction.CCW)
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

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
    }
}