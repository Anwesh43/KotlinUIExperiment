package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * Created by anweshmishra on 04/10/17.
 */
class ImageCircleClipperView(ctx:Context,var bitmap:Bitmap):View(ctx) {
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
    data class ImageClipperView(var bitmap:Bitmap,var size:Float,var cx:Float = bitmap.width.toFloat()/2,var cy:Float = bitmap.height.toFloat()/2) {
        var rectPixels:LinkedList<Pixel> = LinkedList()
        var circlePixels:LinkedList<Pixel> = LinkedList()
        fun draw(canvas:Canvas,paint:Paint) {
            if(rectPixels.size == 0) {
                separatePixels()
            }
            rectPixels.forEach { pixel ->
                pixel.draw(canvas,paint)
            }
            canvas.save()
            canvas.translate(cx,cy)
            canvas.rotate(180f)
            canvas.save()
            canvas.translate(-cx,-cy)
            circlePixels.forEach { pixel ->
                pixel.draw(canvas,paint)
            }
            canvas.restore()
            canvas.restore()
        }
        private fun separatePixels() {
            for(i in 0..bitmap.width-1) {
                for(j in 0..bitmap.height-1) {
                    val color = bitmap.getPixel(i,j)
                    val pixel = Pixel(i.toFloat(),j.toFloat(),color)
                    var distance = getDistance(i.toFloat(),j.toFloat())
                    if(distance>size/2) {
                        rectPixels.add(pixel)
                    }
                    else {
                        circlePixels.add(pixel)
                    }
                }
            }
        }
        private fun getDistance(x:Float,y:Float):Float {
            return Math.sqrt(Math.pow((cx-x).toDouble(),2.0)+Math.pow((cy-y).toDouble(),2.0)).toFloat()
        }
    }
    data class Pixel(var x:Float,var y:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = color
            canvas.drawRect(RectF(x,y,x+1,y+1),paint)
        }
    }
}