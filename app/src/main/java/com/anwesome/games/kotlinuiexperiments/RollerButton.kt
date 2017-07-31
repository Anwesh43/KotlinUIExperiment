package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 31/07/17.
 */
class RollerButton(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class RBRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
            }
            time++
        }
        fun handleTap() {

        }
    }
    data class RollerIndicator(var w:Float,var h:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#01579B")
            canvas.drawRect(RectF(0.0f,0.0f,w*scale,h),paint)
        }
    }
    data class RBButton(var x:Float,var y:Float,var r:Float,var w:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            paint.color = Color.parseColor("#9E9E9E")
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.WHITE
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(i*90.0f)
                canvas.drawLine(0.0f,-r/2,0.0f,r/2,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            deg = 360.0f*scale
            x = w*scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class RollerButtonShape(var indicator: RollerIndicator,var button:RBButton) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            indicator.draw(canvas,paint,scale)
            button.draw(canvas,paint)
            button.update(scale)
        }
        fun handleTap(x:Float,y:Float):Boolean = button.handleTap(x,y)
    }
    class RBState(var deg:Float = 0.0f,var scale :Float = 0.0f) {
        fun update() {
            deg += 18.0f
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
        }
        fun stopped():Boolean {
            var condition = deg>180
            if(condition) {
                deg = 0.0f
            }
            return condition
        }
    }
    class RBAnimHandler(var shape:RollerButtonShape,var v:RollerButton,var animated:Boolean = true,var state:RBState = RBState()) {
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun startUpdating(x:Float,y:Float) {
            if(!animated && shape.handleTap(x,y)) {
                animated = true
                v.postInvalidate()
            }
        }
        fun draw(canvas: Canvas,paint: Paint) {
            shape.draw(canvas,paint,state.scale)
        }
    }
}