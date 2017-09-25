package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 25/09/17.
 */
class SwiperSquareView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = SwiperSquareRenderer(this)
    val detector = GestureDetector(ctx,GestureListener(renderer))
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event)
    }
    data class GestureListener(var renderer:SwiperSquareRenderer):GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event:MotionEvent):Boolean {
            renderer.handleTap(event.x,event.y)
            return true
        }
        override fun onSingleTapUp(event:MotionEvent):Boolean {
            return true
        }
        override fun onFling(e1:MotionEvent,e2:MotionEvent,velx:Float,vely:Float):Boolean {
            if(Math.abs(velx)>Math.abs(vely) && e1.x != e2.x) {
                val diff = e2.x-e1.x
                renderer.startUpdating((diff)/Math.abs(diff))
            }
            return true
        }
    }
    data class SwiperSquare(var y:Float,var w:Float,var x:Float = w/2,var isSelected:Boolean = false,var state:SwiperSquareState = SwiperSquareState(x,w)) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#FFEB3B")
            if(isSelected) {
                paint.color = Color.parseColor("#009688")
            }
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRect(-w/10,-w/10,w/10,w/10,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating(dir:Float) {
            state.setSwiperDirection(dir)
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-w/10 && x<=this.x+w/10 && y>=this.y-w/10 && y<=this.y+w/10 && !isSelected && state.dir == 0f
    }
    data class SwiperSquareState(var x:Float=0f,var w:Float,var dir:Float = 0f) {
        fun update() {
            x += (w/13)*dir
            if(x>w) {
                dir = 0f
            }
            if(x<0) {
                dir = 0f
            }
        }
        fun setSwiperDirection(dir:Float) {
            this.dir = dir
        }
        fun stopped():Boolean = dir == 0f
    }
    class SwiperSquareRenderer(var view:SwiperSquareView,var time:Int = 0) {
        var swiperSquareContainer:SwiperSquareContainer? = null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                swiperSquareContainer = SwiperSquareContainer(w,h,view)
            }
            if((time+1)% 30 == 0) {
                swiperSquareContainer?.create()
            }
            swiperSquareContainer?.draw(canvas,paint)
            swiperSquareContainer?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            swiperSquareContainer?.handleTap(x,y)
        }
        fun startUpdating(dir:Float) {
            swiperSquareContainer?.setSwiperDirection(dir)
        }
    }
    class SwiperSquareContainer(var w:Float,var h:Float,var view:SwiperSquareView,var swiperSquares:ConcurrentLinkedQueue<SwiperSquare> = ConcurrentLinkedQueue()) {
        var tappedSwiperSquares:ConcurrentLinkedQueue<SwiperSquare>  = ConcurrentLinkedQueue()
        var animated:Boolean = false
        var updatedSwiperSquares = ConcurrentLinkedQueue<SwiperSquare>()
        fun create() {
            var random = Random()
            var y = (random.nextInt(h.toInt())).toFloat()
            swiperSquares.add(SwiperSquare(y,w))
        }
        fun draw(canvas: Canvas,paint:Paint) {
            swiperSquares.forEach { swiperSquare ->
                swiperSquare.draw(canvas,paint)
            }
        }
        fun update() {
            if(animated) {
                updatedSwiperSquares.forEach { square ->
                    square.update()
                    if (square.stopped()) {
                        updatedSwiperSquares.remove(square)
                        if (updatedSwiperSquares.size == 0) {
                            animated = false
                        }
                    }
                }
                try {
                    Thread.sleep(50)
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            swiperSquares.forEach { swiperSquare ->
                if(swiperSquare.handleTap(x,y)) {
                    tappedSwiperSquares.add(swiperSquare)
                }
            }
        }
        fun setSwiperDirection(dir:Float) {
            val firstSquare = updatedSwiperSquares.size == 0
            tappedSwiperSquares.forEach { swiperSquare ->
                swiperSquare.startUpdating(dir)
                updatedSwiperSquares.add(swiperSquare)
                tappedSwiperSquares.remove(swiperSquare)
            }
            if(firstSquare){
                animated = true
                view.postInvalidate()
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = SwiperSquareView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y))
        }
    }
}