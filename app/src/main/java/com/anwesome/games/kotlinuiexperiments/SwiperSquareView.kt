package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 25/09/17.
 */
class SwiperSquareView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
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
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-w/10 && x<=this.x+w/10 && y>=this.y-w/10 && y<=this.y+w/10
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
    class SwiperStateRenderer(var view:Float = 0f,var time:Int = 0) {
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width
                val h = canvas.height
            }
            if((time+1)% 30 == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
        fun startUpdating(dir:Float) {

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
}