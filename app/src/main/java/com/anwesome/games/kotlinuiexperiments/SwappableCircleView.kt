package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 14/08/17.
 */
class SwappableCircleView(ctx:Context,var n:Int = 3):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

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
            x = traversePath?.x?:x
            y = traversePath?.y?:y
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class TraversePath(var x:Float,var y:Float,var r:Float,var deg:Float) {
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
            if(first != null && second != null) {
                first?.update()
                second?.update()
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
                            second = first
                            var x = ((first?.x?:0.0f).toFloat() + (second?.x?:0.0f).toFloat())/2
                            var y = ((first?.y?:0.0f).toFloat() + (second?.y?:0.0f).toFloat())/2
                            var r =  ((first?.x?:0.0f).toFloat() - (second?.x?:0.0f).toFloat())/2
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
                    circles.add(SwappableCircle(x,y,2*gap/3))
                }
                controller = SwapableCircleRenderController(circles,h,v)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
}