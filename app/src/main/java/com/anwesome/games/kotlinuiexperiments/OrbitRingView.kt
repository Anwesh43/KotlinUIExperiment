package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 13/07/17.
 */
class OrbitRingView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer:ORVRenderer = ORVRenderer()
    override fun onDraw(canvas: Canvas) {
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
    class ORVRenderer {
        var time = 0
        var drawingController:DrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:OrbitRingView) {
            if(time == 0) {
                drawingController = DrawingController(canvas.width.toFloat(),canvas.height.toFloat(),v)
            }
            drawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.handleTap(x,y)
        }
    }
    class DrawingController(w:Float,h:Float,var v:OrbitRingView) {
        var animated = false
        var orbitRing:OrbitRing?=null
        init {
            orbitRing = OrbitRing(w/2,h/2,Math.min(w,h)/2)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            orbitRing?.draw(canvas,paint)
            if(animated) {
                orbitRing?.update()
                if(orbitRing?.stopped()?:false) {
                    animated = false
                }
                try {
                    Thread.sleep(70)
                    v?.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(orbitRing?.handleTap(x,y)?:false &&  !animated) {
                orbitRing?.startUpdate()
                animated = true
                v?.postInvalidate()
            }
        }
    }
    data class OrbitRing(var x:Float,var y:Float,var size:Float) {
        var scale = 0.0f
        var dir = 0
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(360*scale)
            paint.style = Paint.Style.STROKE
            paint.color = Color.GRAY
            paint.strokeWidth = size/60
            canvas.drawCircle(0.0f,0.0f,size/2,paint)
            paint.color = Color.BLUE
            var path:Path = Path()
            path.moveTo(0.0f,-size/2)
            for(i in 0..(360*scale).toInt()) {
                var x = (size/2)*(Math.cos((i-90)*Math.PI/180)).toFloat()
                var y = (size/2)*(Math.sin((i-90)*Math.PI/180)).toFloat()
                path.lineTo(x,y)
            }
            canvas.drawPath(path,paint)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(0.0f,-size/2,size/9,paint)
            canvas.restore()
        }
        fun update() {
            scale += dir * 0.1f
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun stopped():Boolean {
            return dir == 0
        }
        fun startUpdate() {
            if(scale <= 0.0f) {
                dir = 1
            }
            if(scale >= 1.0f) {
                dir = -1
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-this.size/2-size/9 && y<=this.y-size/2+size/9;
    }
}
