package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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
    data class ImageClipperView(var bitmap:Bitmap,var size:Float) {
        var rectPixels:LinkedList<Int> = LinkedList()
        var circlePixels:LinkedList<Int> = LinkedList()
        fun draw(canvas:Canvas,paint:Paint) {
            if(rectPixels.size == 0) {
                separatePixels()
            }
            rectPixels.forEach { pixel ->
                paint.color = pixel
                canvas.drawRect(0f,0f,1f,1f,paint)
            }
        }
        private fun separatePixels() {
            for(i in 0..bitmap.width-1) {
                for(j in 0..bitmap.height-1) {
                    val pixel = bitmap.getPixel(i,j)
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
            val cx = bitmap.width/2
            val cy = bitmap.height/2
            return Math.sqrt(Math.pow((cx-x).toDouble(),2.0)+Math.pow((cy-y).toDouble(),2.0)).toFloat()
        }
    }
}