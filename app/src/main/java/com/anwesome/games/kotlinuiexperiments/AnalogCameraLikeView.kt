package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 07/11/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
class AnalogCameraLikeView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class AnalogCameraShape(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawRoundRect(RectF(-w/3,-w/4,w/3,w/4),w/10,w/10,paint)
            paint.color = Color.parseColor("#212121")
            canvas.drawCircle(0f,0f,w/5,paint)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawCircle(0f,0f,w/10,paint)
            canvas.drawRoundRect(RectF(-w/20,-w/3-w/20,w/20,-w/3),w/30,w/30,paint)
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
        fun handleTap(x:Float,y:Float):Boolean = x>=w/2-w/5 && x<=w/2+w/5 && y>=h/2-w/5 && y<=h/2+w/5
    }
    data class AnalogCameraState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f){
        fun update() {
            scale += dir*0.1f
            if(Math.abs(prevScale-scale) > 1) {
                dir = 0f
                scale = (prevScale+1)%2
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*this.scale
        }
    }
}