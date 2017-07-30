package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 30/07/17.
 */
class ScaleUpColorFilterImageView(ctx:Context,var color:Int=Color.CYAN,var bitmap:Bitmap):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class ColorFilterImage(var x:Float,var y:Float,var bitmap: Bitmap,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.BLACK
            canvas.drawBitmap(bitmap,-w/2,-h/2,paint)
            canvas.restore()
        }
        private fun drawColorFilter(canvas: Canvas,paint:Paint,color:Int,scale:Float,w:Float,h:Float) {
            paint.color = Color.argb(150,Color.red(color),Color.green(color),Color.blue(color))
            canvas.save()
            canvas.scale(scale,scale)
            canvas.drawRect(RectF(-w/2,-h/2,w/2,h/2),paint)
            canvas.restore()
        }
    }
}