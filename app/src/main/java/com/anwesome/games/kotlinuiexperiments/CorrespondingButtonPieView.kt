package com.anwesome.games.kotlinuiexperiments

import android.content.*
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 22/10/17.
 */
class CorrespondingButtonPieView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class CorrespondingButtonPie(var i:Int,var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,px:Float,py:Float,pr:Float,gap:Float) {
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.translate(px,py)
            canvas.drawArc(RectF(-pr,-pr,pr,pr),i*gap,60f,true,paint)
            canvas.restore()
            paint.style = Paint.Style.STROKE
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.save()
            canvas.scale(1f,1f)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
}