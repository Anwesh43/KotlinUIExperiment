package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 30/07/17.
 */
class ScaleUpColorFilterImageView(ctx:Context,var bitmap:Bitmap,var color:Int=Color.CYAN):View(ctx) {
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
            paint.style = Paint.Style.FILL
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.BLACK
            canvas.drawBitmap(bitmap,-w/2,-h/2,paint)
            this.drawColorFilter(canvas,paint,color,scale,bitmap.width.toFloat(),bitmap.height.toFloat())
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
    data class ScaleUpIndicator(var x:Float,var y:Float,var size:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(180.0f*scale+45)
            paint.style = Paint.Style.STROKE
            this.drawDirectionIndicator(canvas,paint)
            canvas.restore()
        }
        private fun drawDirectionIndicator(canvas:Canvas,paint:Paint) {
            paint.color = Color.BLACK
            paint.strokeWidth = size/8
            paint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(0.0f,-size/2,0.0f,size/2,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(2*i-1.0f,1.0f)
                canvas.drawLine(0.0f,-size/2,size/6,-size/3,paint)
                canvas.restore()
            }
        }
        fun handleTap(x:Float,y:Float):Boolean =  x>=this.x-size/2 && x<=this.x+size/2 && y>=this.y-size/2 && y<=this.y+size/2
    }
    data class ScaleUpIndicatorColorFilterImage(var scaleUpIndicator:ScaleUpIndicator,var colorFilterImage:ColorFilterImage) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            colorFilterImage.draw(canvas,paint,scale)
            scaleUpIndicator.draw(canvas,paint,scale)
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
                var sicfb = ScaleUpIndicatorColorFilterImage(ScaleUpIndicator(sizeW/2,sizeH/2,Math.min(sizeH,sizeW)/4), ColorFilterImage(sizeW/2,sizeH/2,bitmap,v.color))
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
            if(!animated && sicfb.handleTap(x,y)) {
                state.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }

    }
    companion object {
        fun create(activity:Activity,bitmap: Bitmap) {
            var view =  ScaleUpColorFilterImageView(activity,bitmap)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
}