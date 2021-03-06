package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

/**
 * Created by anweshmishra on 18/07/17.
 */
class DownloadButtonView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer:DBVRenderer = DBVRenderer()
    var downloadListener:DownloadButtonListener?=null
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
    data class DownloadButtonShape(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0.0f,0.0f,r,paint)
            paint.color = Color.parseColor("#03A9F4")
            canvas.drawArc(RectF(-r,-r,r,r),-90.0f,360*scale,true,paint)
            paint.color = Color.WHITE
            canvas.drawLine(0.0f,-r/2,0.0f,r/2,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.translate(0.0f, r / 2)
                canvas.rotate(45.0f*(i*2-1))
                canvas.drawLine(0.0f,0.0f,0.0f,-r/2,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean =  x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class DBVRenderer {
        var time = 0
        var dbvDrawingController:DBVDrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:DownloadButtonView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
                paint.strokeWidth = (Math.min(w,h)/60).toFloat()
                paint.strokeJoin = Paint.Join.ROUND
                paint.strokeCap = Paint.Cap.ROUND
                dbvDrawingController = DBVDrawingController(DownloadButtonShape((w/2).toFloat(),(h/2).toFloat(),(Math.min(w,h)/4).toFloat()),v)
            }
            dbvDrawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            dbvDrawingController?.handleTap(x,y)
        }
    }
    class DBVDrawingController(var shape:DownloadButtonShape ,var view:DownloadButtonView) {
        var animated = false
        var stateContainer = StateContainer()
        fun draw(canvas:Canvas,paint:Paint) {
            shape.draw(canvas,paint,stateContainer.scale)
            if(animated) {
                try {
                    stateContainer.update()
                    if(stateContainer.stopped()) {
                        animated = false
                        when(stateContainer.scale) {
                            0.0f -> {
                                view.downloadListener?.onUnInstallIndicator()
                            }
                            1.0f -> {
                                view.downloadListener?.onInstallIndicator()
                            }
                        }
                    }
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && shape.handleTap(x,y)) {
                stateContainer.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class StateContainer {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun startUpdating() {
            dir = 1
            if(scale >= 1) {
                dir = -1
            }
        }
        fun stopped():Boolean = dir == 0
    }
}
interface DownloadButtonListener {
    fun onInstallIndicator() {

    }
    fun onUnInstallIndicator() {

    }
}