package com.anwesome.games.kotlinuiexperiments

import android.app.Notification
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 08/07/17.
 */
class TouchMeterView(ctx:Context):View(ctx){
    var renderer = TMVRenderer()
    val  paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        renderer.handleTap(event,this)
        return true
    }
    class TMVRenderer {
        var time = 0
        var down = false
        var drawingController:TMVDrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:TouchMeterView) {
            if(time == 0) {
                drawingController = TMVDrawingController(canvas.width.toFloat(),canvas.height.toFloat(),v)
            }
            drawingController?.render(canvas,paint)
            time++
        }
        fun handleTap(event:MotionEvent,v:View) {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(!down && drawingController?.checkDown(event.x,event.y)?:false) {
                        down = true
                        v?.postInvalidate()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(down) {
                        drawingController?.handleTouchUp()
                        down = false
                    }
                }
            }
        }
    }
    class TMVDrawingController {
        var touchMeter:TouchMeter?=null
        var animated = false
        var v:TouchMeterView?=null
        constructor(w:Float,h:Float,v:TouchMeterView) {
            touchMeter = TouchMeter(w/2,h/2,Math.min(w,h)/4)
            this.v = v
        }
        fun render(canvas:Canvas,paint:Paint) {
            touchMeter?.draw(canvas, paint)
            if(animated) {
                touchMeter?.update()
                if(touchMeter?.stopped()?:false) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    v?.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun checkDown(x:Float,y:Float):Boolean {
            var condition = touchMeter?.handleTap(x,y)?:false
            if(condition) {
                touchMeter?.startUpdating(1.0f)
            }
            return condition
        }
        fun handleTouchUp() {
            touchMeter?.startUpdating(-1.0f)
        }

    }
    data class TouchMeter(var x:Float,var y:Float,var size:Float) {
        var scale = 0.0f
        var dir = 0.0f
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#FF5722")
            paint.strokeWidth = size/8
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0.0f,0.0f,size/8,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawCircle(0.0f,0.0f,size/8,paint)
            canvas.restore()
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1.0f,(i*2-1)*1.0f)
                paint.style = Paint.Style.STROKE
                canvas.drawRect(-size/16,size/8,size/16,size/8+3*size/8,paint)
                paint.style = Paint.Style.FILL
                canvas.drawRect(-size/16,size/8,size/16,size/8+(3*size/8)*scale,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun startUpdating(dir:Float) {
            this.dir = dir
        }
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1.0f
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0.0f
            }
        }
        fun stopped():Boolean = dir == 0.0f
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/8 && x<=this.x+size/8 && y>=this.y-size/8  && y<=this.y+size/8 && dir == 0.0f && scale == 0.0f
    }
}