package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 06/07/17.
 */
class CircularLoaderListView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event: MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class Renderer {
        var time = 0
        fun render(canvas:Canvas?,paint:Paint,v:CircularLoaderListView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {
            
        }
    }
    data class CircularLoader(var x:Float,var y:Float,var r:Float) {
        var startDeg = 0.0f
        var endDeg = 0.0f
        var dir = 0.0f
        var stopped = false
        fun draw(canvas:Canvas?,paint:Paint) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = r/40
            canvas?.save()
            canvas?.translate(x,y)
            canvas?.drawArc(RectF(-r,-r,r,r),0.0f,360.0f,false,paint)
            paint.color = Color.WHITE
            canvas?.drawArc(RectF(-r,-r,r,r),startDeg-90,endDeg,false,paint)
            canvas?.restore()
        }
        fun update() {
            if(startDeg < 300) {
                endDeg+=10*dir
            }
            if(endDeg >= 90) {
                startDeg+=10*dir
            }
            if(startDeg >= 360) {
                startDeg = 360.0f
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
        var animated = false
        var loaders:ConcurrentLinkedQueue<CircularLoader> = ConcurrentLinkedQueue()
        var tappedLoaders:ConcurrentLinkedQueue<CircularLoader> = ConcurrentLinkedQueue()
        var v:CircularLoaderListView?=null
        constructor(w:Int,h:Int,v:CircularLoaderListView) {
            this.w = w
            this.h = h
            this.sizeOfEachLoader = (Math.min(w,h)/20).toFloat()
            this.v = v
        }
        fun render(canvas:Canvas?,paint:Paint) {
            loaders.forEach { loader ->
                loader.draw(canvas,paint)
            }
            if(animated) {
                tappedLoaders?.forEach { tappedLoader ->
                    tappedLoader.update()
                    if(tappedLoader.stopped) {
                        tappedLoaders.remove(tappedLoader)
                        loaders.remove(tappedLoader)
                        if(tappedLoaders.size == 1) {
                            animated = false
                        }
                    }
                }
                try {
                    Thread.sleep(50)
                    v?.invalidate()
                }
                catch (e:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            loaders.forEach { loader->
                if(loader.handleTap(x,y)) {
                    tappedLoaders.add(loader)
                    loader.dir = 1.0f
                    if(tappedLoaders.size == 1) {
                        animated = true
                        v?.postInvalidate()
                    }
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