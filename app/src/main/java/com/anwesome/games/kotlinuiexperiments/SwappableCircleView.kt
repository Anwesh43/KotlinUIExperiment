package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 14/08/17.
 */
class SwappableCircleView(ctx:Context,var n:Int = 3):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = SwappableViewRenderer()
    override fun onDraw(canvas: Canvas) {
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
    data class SwappableCircle(var x:Float,var y:Float,var r:Float) {
        var traversePath:TraversePath?=null
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#00838F")
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun update() {
            traversePath?.update()
            x = traversePath?.traversePoint?.x?:x
            y = traversePath?.traversePoint?.y?:y
        }
        fun stopped():Boolean = traversePath?.stopped()?:false
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-2*r && x<=this.x+2*r && y>=this.y-2*r && y<=this.y+2*r
    }
    data class TraversePath(var x:Float,var y:Float,var r:Float,var deg:Float=0.0f) {
        var updeg = 0.0f
        var traversePoint = PointF()
        fun update() {
            traversePoint.x = x+r*(Math.cos((deg+updeg)*Math.PI/180)).toFloat()
            traversePoint.y = y+r*(Math.sin((deg+updeg)*Math.PI/180)).toFloat()
            updeg += 20.0f
        }
        fun stopped():Boolean =  updeg > 180.0f
    }
    data class SwapableCircleRenderController(var circles:ConcurrentLinkedQueue<SwappableCircle>,var h:Float,var v:SwappableCircleView) {
        var first:SwappableCircle?=null
        var second:SwappableCircle?=null
        fun update() {
            first?.update()
            second?.update()
            if(first != null && second != null) {
                if(first?.stopped()?:false || second?.stopped()?:false) {
                    first = null
                    second = null
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
            circles.forEach { circle ->
                circle.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!(first != null && second!=null)) {
                circles.forEach { circle ->
                    if(circle.handleTap(x,y)) {
                        if(first != null) {
                            second = circle
                            var x = ((first?.x?:0.0f).toFloat() + (second?.x?:0.0f).toFloat())/2
                            var y = ((first?.y?:0.0f).toFloat() + (second?.y?:0.0f).toFloat())/2
                            var r =  Math.abs((first?.x?:0.0f).toFloat() - (second?.x?:0.0f).toFloat())/2
                            var tpath1 = TraversePath(x,y,r)
                            var tpath2 = TraversePath(x,y,r)
                            if(first?.x?:0.0f > second?.x?:0.0f) {
                                tpath1.deg = 0.0f
                                tpath2.deg = 180.0f
                            }
                            else {
                                tpath2.deg = 0.0f
                                tpath1.deg = 180.0f
                            }
                            first?.traversePath = tpath1
                            second?.traversePath = tpath2
                            v.postInvalidate()
                            return
                        }
                        else {
                            first = circle
                            return
                        }
                    }
                }
            }
        }
    }
    class SwappableViewRenderer {
        var time = 0
        var controller:SwapableCircleRenderController?=null
        fun render(canvas: Canvas,paint: Paint,v:SwappableCircleView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var gap = w/(2*v.n+1)
                var x = 3*gap/2
                var y = h/2
                var circles:ConcurrentLinkedQueue<SwappableCircle> = ConcurrentLinkedQueue()
                for(i in 0..v.n) {
                    circles.add(SwappableCircle(x,y,gap/3))
                    x += 2*gap
                }
                controller = SwapableCircleRenderController(circles,h,v)
            }
            controller?.draw(canvas,paint)
            time++
            controller?.update()

        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity: Activity,vararg ns:Int) {
            var view = SwappableCircleView(activity)
            if(ns.size == 1) {
                view.n = ns[0]
            }
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y))
        }
    }
}