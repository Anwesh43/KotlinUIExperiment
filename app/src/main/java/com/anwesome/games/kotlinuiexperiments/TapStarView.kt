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
        var controller:TPVController?=null
        fun render(canvas:Canvas,paint:Paint,v:TapStarView) {
            if(time == 0) {
                paint.color = Color.parseColor("#ffc107")
                controller = TPVController(canvas.width.toFloat(),canvas.height.toFloat(),v)
            }
            controller?.draw(canvas,paint)
            if(time % 25 == 5) {
                controller?.createStars()
            }
            time++
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
    data class TapStar(var x:Float,var y:Float,var size:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            var path = Path()
            for(i in 0..5) {
                var sx = (size/2)*Math.cos(i*60*Math.PI/180)
                var sy = (size/2)*Math.sin(i*60*Math.PI/180)
                if(i == 0) {
                    path.moveTo(sx.toFloat(), sy.toFloat())
                }
                else {
                    path.lineTo(sx.toFloat(),sy.toFloat())
                }
            }
            canvas.drawPath(path,paint)
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