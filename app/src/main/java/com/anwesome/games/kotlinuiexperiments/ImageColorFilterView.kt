package com.anwesome.games.kotlinuiexperiments

import android.view.View
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent

/**
 * Created by anweshmishra on 11/07/17.
 */
class ImageColorFilterView(ctx:Context,var bitmap:Bitmap,var color:Int=Color.BLUE):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
    class ICFVRenderer {
        var time = 0
        fun render(canvas:Canvas,paint:Paint,v:ImageColorFilterView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
            }
            time++
        }
        fun handleTap() {

        }
    }
    class DrawingController(w:Float,h:Float,bitmap:Bitmap,var v:ImageColorFilterView) {
        var animated = false
        init {

        }
        fun draw(canvas:Canvas,paint:Paint) {
            if(animated) {
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                v.postInvalidate()
            }
        }
    }
    data class ImageColorFilter(var b:Bitmap,var color:Int,var x:Float,var y:Float) {
        var scale:Float = 0.0f
        var dir:Int = 0
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.BLACK
            canvas.drawBitmap(b,-b.width.toFloat()/2,-b.height.toFloat()/2,paint)
            canvas.save()
            canvas.scale(scale,scale)
            paint.color = Color.argb(120,Color.red(color),Color.green(color),Color.blue(color));
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            scale += 0.15f*dir
            if(scale > 1) {
                scale = 1.0f
                dir = 0
            }
            if(scale < 0) {
                scale = 0.0f
                dir = 0
            }
        }
        fun stopped():Boolean = dir == 0
        fun handleTap() {
            if(dir == 0) {
                dir = when(scale) {
                    1.0f->-1
                    else->1
                }
            }
        }
    }
}