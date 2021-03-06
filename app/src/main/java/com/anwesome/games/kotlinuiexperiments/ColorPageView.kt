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
    var renderer:Renderer = Renderer()
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    fun addColor(color:Int) {
        colors.add(color)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,colors,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
}
class Renderer {
    var time:Int = 0
    var animationHandler:AnimationHandler?=null
    fun render(canvas:Canvas,paint:Paint,colors:List<Int>,v:ColorPageView) {
        if(time == 0) {
            animationHandler = AnimationHandler(colors,v)
        }
        animationHandler?.animate(canvas,paint,canvas.width,canvas.height)
        time++
    }
    fun handleTap() {
        animationHandler?.startAnimation()
    }
}
data class ColorPage(var color:Int) {
    var scale:Float = 1.0f
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
    constructor(colors:List<Int>,v:ColorPageView) {
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
                colorPages = colorPages?.filter{ colorPage->
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
        if(!animated && colorPages?.isEmpty() == false) {
            curr = colorPages?.last()
            curr?.dir = -1.0f
            animated = true
            v?.postInvalidate()
        }
    }
}