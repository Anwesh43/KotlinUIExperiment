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
 * Created by anweshmishra on 04/08/17.
 */
class LockerView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = LVRenderer()
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
    class LVRenderer {
        var time = 0
        var handler:LVAnimationHandler?=null
        fun render(canvas:Canvas,paint:Paint,v:LockerView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                handler = LVAnimationHandler(Locker(w/2,h/2,Math.min(w,h)/3),v)
            }
            handler?.draw(canvas,paint)
            handler?.update()
            time++
        }
        fun handleTap(x:Float,y:Float)  {
            handler?.startUpdating(x,y)
        }
    }
    data class Locker(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/30
            paint.color = Color.WHITE
            canvas.drawCircle(x,y,r,paint)
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#01579B")
            canvas.drawArc(RectF(-r,-r,r,r),-90.0f,360.0f*scale,false,paint)
            paint.style = Paint.Style.FILL
            canvas.drawRect(RectF(-r/4,0.0f,r/4,r/2),paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/20
            canvas.save()
            canvas.translate(-r/8,0.0f)
            canvas.rotate(-90*scale)
            canvas.drawArc(RectF(0.0f,-r/4,r/4,r/4),180.0f,180.0f,false,paint)
            canvas.restore()
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class LVAnimationHandler(var locker:Locker,var v:LockerView) {
        var animated = false
        var state = LVStateHandler()
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
        fun draw(canvas:Canvas,paint:Paint) {
            locker.draw(canvas,paint,state.scale)
        }
        fun startUpdating(x:Float,y:Float) {
            if(!animated && locker.handleTap(x,y)) {
                state.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    class LVStateHandler {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += dir * 0.2f
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                dir = 0
                scale = 0.0f
            }
        }
        fun startUpdating() {
            if(dir == 0) {
                dir = (1-2*scale).toInt()
            }
        }
        fun stopped():Boolean = dir == 0
    }
    companion object {
        fun create(activity: Activity) {
            var view = LockerView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
}