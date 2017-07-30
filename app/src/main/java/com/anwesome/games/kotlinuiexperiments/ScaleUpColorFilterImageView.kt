package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 30/07/17.
 */
class ScaleUpColorFilterImageView(ctx:Context,var color:Int=Color.CYAN,var bitmap:Bitmap):View(ctx) {
    val renderer = CFSVRenderer()
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas: Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
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
    data class ScaleUpIndicator(var x:Float,var y:Float,var size:Float,var w:Float,var h:Float,var deg:Float = 0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg+45)
            paint.style = Paint.Style.STROKE
            canvas.restore()
        }
        private fun drawDirectionIndicator(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            paint.strokeWidth = size/20
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(0.0f,-size/3,0.0f,size/3,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1.0f,2*i-1.0f)
                canvas.drawLine(0.0f,-size/3,size/6,-size/6,paint)
                canvas.restore()
            }
        }
        fun update(scale:Float) {
            deg = 180.0f*scale
            x = w*scale
            y = h*(1-scale)
        }
        fun handleTap(x:Float,y:Float):Boolean =  x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
    data class ScaleUpIndicatorColorFilterImage(var scaleUpIndicator:ScaleUpIndicator,var colorFilterImage:ColorFilterImage) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            colorFilterImage.draw(canvas,paint,scale)
            scaleUpIndicator.draw(canvas,paint)
            scaleUpIndicator.update(scale)
        }
        fun handleTap(x:Float,y:Float):Boolean = scaleUpIndicator.handleTap(x,y)
    }
    class CFSVState(var scale:Float = 0.0f,var dir:Int = 0) {
        fun update() {
            scale += 0.2f*dir
            if(scale > 1 || scale < 0) {
                dir = 0
                if(scale > 1) {
                    scale = 1.0f
                }
                else {
                    scale = 0.0f
                }
            }
        }
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
        fun stopped():Boolean = dir == 0
    }
    class CFSVRenderer(var time:Int = 0) {
        var animHandler:CFSVAnimHandler?=null
        fun render(canvas: Canvas,paint: Paint,v:ScaleUpColorFilterImageView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
                var bitmap = Bitmap.createScaledBitmap(v.bitmap,w,h,true)
                var sizeW = w.toFloat()
                var sizeH = h.toFloat()
                var sicfb = ScaleUpIndicatorColorFilterImage(ScaleUpIndicator(sizeW/2,sizeH/2,Math.min(sizeH,sizeW)/10,sizeW,sizeH), ColorFilterImage(sizeW/2,sizeH/2,bitmap,v.color))
                animHandler = CFSVAnimHandler(sicfb,v)
            }
            animHandler?.draw(canvas,paint)
            animHandler?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animHandler?.handleTap(x,y)
        }
    }
    class CFSVAnimHandler(var sicfb:ScaleUpIndicatorColorFilterImage,var v:ScaleUpColorFilterImageView,var state:CFSVState = CFSVState(),var animated:Boolean = false)  {
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    v.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            sicfb.draw(canvas,paint,state.scale)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated) {
                sicfb.handleTap(x,y)
                animated = true
                v.postInvalidate()
            }
        }

    }
}