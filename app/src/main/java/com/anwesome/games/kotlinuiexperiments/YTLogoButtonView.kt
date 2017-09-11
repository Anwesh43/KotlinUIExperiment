package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 12/09/17.
 */
class YTLogoButtonView(ctx:Context):View(ctx) {
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
    data class YTLogoButton(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.rgb(255,0,0)
            canvas.drawRoundRect(RectF(-size/2,size/2,size/2,size/2),size/10,size/10,paint)
            paint.color = Color.rgb(255*(1-1),0,0)
            canvas.save()
            canvas.rotate(90f)
            var path = Path()
            path.moveTo(-size/10,-size/10)
            path.lineTo(size/10,0f)
            path.lineTo(-size/10,size/10)
            path.lineTo(-size/10,-size/10)
            canvas.drawPath(path,paint)
            canvas.restore()
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x - size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
        fun update() {

        }
        fun stopped():Boolean = false
    }
}