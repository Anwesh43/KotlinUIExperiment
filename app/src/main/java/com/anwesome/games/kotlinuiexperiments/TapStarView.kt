package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 02/08/17.
 */
class TapStarView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    class TPVRenderer {
        var time =  0
        fun render(canvas:Canvas,paint:Paint,v:TapStarView) {
            if(time == 0) {

            }
            time++
        }
        fun handleTap(x:Float,y:Float) {

        }
    }
    data class TapStar(var x:Float,var y:Float,var size:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            canvas.restore()
        }
        fun update() {
            deg += 20
            y += 25
        }
        fun stopUpdating(h:Float):Boolean {
            return y>=h
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
    class TPVController(var w:Float,var h:Float,var v:TapStarView,var stars:ConcurrentLinkedQueue<TapStar> = ConcurrentLinkedQueue()) {
        var random = Random()
        fun draw(canvas:Canvas,paint:Paint) {
            stars.forEach { star ->
                star.draw(canvas,paint)
                star.update()
                if(star.stopUpdating(h)) {
                    stars.remove(star)
                }
            }
        }
        fun createStars() {
            stars.add(TapStar(random.nextInt(w.toInt()).toFloat(),-w/10,w/5))
        }
        fun handleTap(x:Float,y:Float) {
            stars.forEach { star ->
                star.handleTap(x,y)
                stars.remove(star)
            }
        }
    }
}