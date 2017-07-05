package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
/**
 * Created by anweshmishra on 05/07/17.
 */
class ColorPageView(ctx:Context):View(ctx) {
    var colors:ArrayList<Int> = ArrayList()
    fun addColor(color:Int) {
        colors.add(color)
    }
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}
class Renderer {
    var time:Int = 0
    fun render(canvas:Canvas,colors:List<Int>,v:ColorPageView) {
        if(time == 0) {

        }
        time++
    }
    fun handleTap() {

    }
}
data class ColorPage(var color:Int) {
    var scale:Float = 0.0f
    var dir:Float = 0.0f
    fun draw(canvas:Canvas,paint:Paint,w:Int,h:Int) {
        paint.color = color
        canvas.drawRect(RectF(0.0f,0.0f,w.toFloat(),h*scale),paint)
    }
    fun update() {
        scale+=0.2f*dir
        if(scale < 0) {
            scale = 0.0f
            dir = 0.0f
        }
    }
}
class AnimationHandler{
    var colorPages:List<ColorPage>?=null
    var animated = false
    var curr:ColorPage?=null
    var v:ColorPageView?=null
    constructor(colors:ArrayList<Int>,v:ColorPageView) {
        colorPages = colors.map{ color -> ColorPage(color) }
        this.v = v
    }
    fun animate(canvas:Canvas,paint:Paint,w:Int,h:Int) {
        colorPages?.forEach { colorPage ->
            colorPage.draw(canvas,paint,w,h)
        }
        if(animated) {
            curr?.update()
            if(curr?.dir == 0.0f) {
                animated = false
                colorPages = colorPages.filter{ colorPage->
                    colorPage!=curr
                }
            }
            try {
                Thread.sleep(50)
                v?.invalidate()
            }
            catch (ex:Exception) {

            }
        }
    }
    fun startAnimation() {
        if(!animated && !colorPages.isEmpty()) {
            curr = colorPages?.get(0)
            animated = true
            v?.postInvalidate()
        }
    }
}