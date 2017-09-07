package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 08/09/17.
 */
class ChromeButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ChromeButtonRenderer()
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
    data class ChromeButton(var x:Float,var y:Float,var size:Float,var state:ChromeButtonState=ChromeButtonState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            val colors = arrayOf("#f44336","#FFD740","#388E3C")
            var deg = 0f
            colors.forEach{ color ->
                paint.color = Color.parseColor(color)
                canvas.drawArc(RectF(-size/3,-size/3,size/3,size/3),deg,120f*state.scale,true,paint)
                deg += 120f
            }
            paint.color = Color.parseColor("#0288D1")
            canvas.drawCircle(0f,0f,size/5,paint)
            paint.color = Color.argb(100,255,255,255)
            canvas.drawArc(RectF(-size/5,-size/5,size/5,size/5),0f,360f*(1-state.scale),true,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float) = x>=this.x - size/5 && x<=this.x+size/5 && y>=this.y -size/5 && y<=this.y+size/5
    }
    data class ChromeButtonState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0){
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
    class ChromeButtonAnimator(var chromeButton:ChromeButton,var view:ChromeButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                chromeButton.update()
                if(chromeButton.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && chromeButton.handleTap(x,y)) {
                chromeButton.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            chromeButton.draw(canvas,paint)
        }
    }
    class ChromeButtonRenderer {
        var chromeButtonAnimator:ChromeButtonAnimator?=null
        var time = 0
        fun render(canvas: Canvas,paint: Paint,view:ChromeButtonView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                chromeButtonAnimator = ChromeButtonAnimator(ChromeButton(w/2,h/2,0.9f*Math.min(w,h)),view)
            }
            chromeButtonAnimator?.draw(canvas,paint)
            chromeButtonAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            chromeButtonAnimator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity) {
            val view = ChromeButtonView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}