package com.anwesome.games.kotlinuiexperiments

import android.view.View
import android.content.Context
import android.graphics.*
import android.view.MotionEvent

/**
 * Created by anweshmishra on 11/07/17.
 */
class ImageColorFilterView(ctx:Context,var bitmap:Bitmap,var color:Int=Color.BLUE):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var renderer:ICFVRenderer = ICFVRenderer()
    var onSelectionListener:OnImageSelectionListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        if(event.action == MotionEvent.ACTION_DOWN) {
            renderer.handleTap(event.x,event.y)
        }
        return true
    }
    class ICFVRenderer {
        var time = 0
        var drawingController:DrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:ImageColorFilterView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
                var size = Math.min(w,h)/2
                v.bitmap = Bitmap.createScaledBitmap(v.bitmap,size,size,true)
                drawingController = DrawingController(w.toFloat(),h.toFloat(),v.bitmap,v)
            }
            drawingController?.draw(canvas,paint)
            time++
        }
        fun handleTap(x:Float,y:Float) {
            drawingController?.handleTap(x,y)
        }
    }
    class DrawingController(w:Float,h:Float,bitmap:Bitmap,var v:ImageColorFilterView) {
        var animated = false
        var imageColorFilter:ImageColorFilter?=null
        init {
            imageColorFilter = ImageColorFilter(bitmap,v.color,w/2,h/2)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            imageColorFilter?.draw(canvas, paint)
            if(animated) {
                imageColorFilter?.update()
                if(imageColorFilter?.stopped()?:false) {
                    if(imageColorFilter?.scale?:0.0f >= 1.0f) {
                        v?.onSelectionListener?.onSelect()
                    }
                    else {
                        v?.onSelectionListener?.onUnselect()
                    }
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && imageColorFilter?.handleTap(x,y)?:false) {
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
            paint.color = Color.argb(120,Color.red(color),Color.green(color),Color.blue(color))
            canvas.drawRect(Rect(-b.width/2,-b.height/2,b.width/2,b.height/2),paint)
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
        fun handleTap(x:Float,y:Float):Boolean {
            val condition = dir == 0 && (x>=this.x-b.width/2 && x<=this.x+b.width/2 && y>=this.y-b.height/2 && y<=this.y+b.height/2)
            if(condition) {
                dir = when (scale) {
                    1.0f -> -1
                    else -> 1
                }
            }
            return condition
        }
    }
}
interface OnImageSelectionListener {
    fun onSelect() {

    }
    fun onUnselect() {

    }
}