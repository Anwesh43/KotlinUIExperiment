package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 22/09/17.
 */
class BitmapExpanderView(ctx:Context):View(ctx) {
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
    data class BitmapExpander(var w:Float,var h:Float,var bitmap:Bitmap,var cx:Float = w/2,var cy:Float = h/20) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(cx,cy)
            canvas.rotate(180f)
            paint.style = Paint.Style.STROKE
            val path = Path()
            path.moveTo(-w/20,w/20)
            path.lineTo(0f,0f)
            path.lineTo(w/20,w/20)
            canvas.drawPath(path,paint)
            canvas.restore()
            canvas.drawBitmap(bitmap,0f,-h/5+h/5,paint)

        }
        fun update() {

        }
        fun startUpdating() {

        }
        fun stopped():Boolean = false
        fun handleTap(x:Float,y:Float):Boolean = x >= this.cx - w/20 && x <= this.cx + w/20 && y>=(cy+h/5)-w/20 && y<=(cy+h/5)+w/20
    }
    data class BitmapExpanderState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
}