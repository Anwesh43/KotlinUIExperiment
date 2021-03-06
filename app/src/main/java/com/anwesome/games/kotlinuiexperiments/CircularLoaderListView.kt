package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 06/07/17.
 */
class CircularLoaderListView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer:Renderer = Renderer()
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event: MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    class Renderer {
        var time = 0
        var loaderRendererController:LoaderRenderController?=null
        fun render(canvas:Canvas,paint:Paint,v:CircularLoaderListView) {
            if(time == 0) {
                loaderRendererController = LoaderRenderController(canvas.width,canvas.height,v)
            }
            loaderRendererController?.render(canvas,paint)
            if(time % 20 == 0) {
                loaderRendererController?.createLoaders()
            }
            time++
        }
        fun handleTap(x:Float,y:Float) {
            loaderRendererController?.handleTap(x,y)
        }
    }
    data class CircularLoader(var x:Float,var y:Float,var r:Float) {
        var startDeg = 0.0f
        var endDeg = 0.0f
        var dir = 0.0f
        var stopped = false
        fun draw(canvas:Canvas?,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/6
            canvas?.save()
            canvas?.translate(x,y)
            paint.color = Color.GRAY
            canvas?.drawArc(RectF(-r,-r,r,r),0.0f,360.0f,false,paint)
            paint.color = Color.WHITE
            canvas?.drawArc(RectF(-r,-r,r,r),startDeg-90,endDeg,false,paint)
            canvas?.restore()
        }
        fun update() {
            if(startDeg < 270 && endDeg < 90) {
                endDeg+=10*dir
            }
            else {
                startDeg+=10*dir
                if(startDeg >= 270) {
                    endDeg -= 10*dir
                }
            }
            if(startDeg >= 360) {
                startDeg = 0.0f
                endDeg = 0.0f
                dir = 0.0f
                stopped = true
            }
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x -r && x<=this.x+r && y>=this.y-r && y<=this.y+r && dir == 0.0f
    }
    class LoaderRenderController {
        var w:Int = 0
        var h:Int = 0
        var sizeOfEachLoader = 0.0f
        var loaders:ConcurrentLinkedQueue<CircularLoader> = ConcurrentLinkedQueue()
        var v:CircularLoaderListView?=null
        constructor(w:Int,h:Int,v:CircularLoaderListView) {
            this.w = w
            this.h = h
            this.sizeOfEachLoader = (Math.min(w,h)/10).toFloat()
            this.v = v
        }
        fun render(canvas:Canvas,paint:Paint) {
            loaders.forEach { loader ->
                loader.draw(canvas,paint)
                loader.update()
                if(loader.stopped) {
                    loaders.remove(loader)
                }
            }
            try {
                Thread.sleep(50)
                v?.invalidate()
            }
            catch (e:Exception) {

            }
        }
        fun handleTap(x:Float,y:Float) {
            loaders.forEach { loader->
                if(loader.handleTap(x,y)) {
                    loader.dir = 1.0f
                }
            }
        }
        fun createLoaders() {
            var random = Random()
            var x:Float = (sizeOfEachLoader+random.nextInt(w-2*sizeOfEachLoader.toInt()))
            var y:Float = (sizeOfEachLoader+random.nextInt(w-2*sizeOfEachLoader.toInt()))
            loaders.add(CircularLoader(x,y,sizeOfEachLoader))
            v?.postInvalidate()
        }
    }
}