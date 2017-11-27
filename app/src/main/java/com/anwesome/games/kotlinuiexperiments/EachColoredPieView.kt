package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 27/11/17.
 */

import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.*

val pieAvailableColors:Array<String> = arrayOf("#f44336","#FFC107","#1565C0","#880E4F","#FF5722","#00E676","#3F51B5","#673AB7")
val defaultPieColorSize = 8
class EachColoredPieView(ctx:Context,var n:Int= defaultPieColorSize):View(ctx) {
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
    data class EachColoredPie(var i:Int,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            paint.color = Color.parseColor(pieAvailableColors[i])
            canvas.drawArc(RectF(-r,-r,r,r),90f*i,90*scale,true,paint)
            canvas.restore()
        }
    }
    data class EachColoredPieContainer(var w:Float,var h:Float,var n:Int) {
        var pies:ConcurrentLinkedQueue<EachColoredPie> = ConcurrentLinkedQueue()
        init {
            val r = Math.min(w,h)/3
            for(i in 0..n) {
                pies.add(EachColoredPie(i,r))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(w/2,h/2)
            pies.forEach { pie ->
                pie.draw(canvas,paint,1f)
            }
            canvas.restore()
        }
        fun update(stopcb:()->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
}