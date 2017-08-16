package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 16/08/17.
 */
class SearchButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class SearchButton(var x:Float,var y:Float,var w:Float,var h:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = w/20
            paint.strokeCap = Paint.Cap.ROUND
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            canvas.drawLine(0.0f,h/2,0.0f,0.0f,paint)
            canvas.drawCircle(0.0f,-h/4,h/4,paint)
            canvas.restore()
        }
        fun update(scale:Float) {
            deg = -45*scale
            x = w*scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-h/2 && x<=this.x+h/2 && y>=this.y-h/2 && y<=this.y+h/2
    }
    data class SearchButtonState(var scale:Float = 0.0f,var deg:Float = 0.0f) {
        fun update() {
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            deg+=20
            if(deg > 180) {
                deg = 0.0f
            }
        }
        fun stopped():Boolean = deg == 0.0f
    }
    class SearchButtonDrawingController(var searchButton:SearchButton,var view:SearchButtonView,var state:SearchButtonState = SearchButtonState(),var animated:Boolean = false) {
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            searchButton.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(searchButton.handleTap(x,y)) {
                animated = true
                view.postInvalidate()
            }
        }
    }
}