package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by anweshmishra on 04/10/17.
 */
class ImageCircleClipperView(ctx:Context,var bitmap:Bitmap):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ImageCircleClipperRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class ImageCircleClipper(var bitmap:Bitmap,var size:Float,var cx:Float = bitmap.width.toFloat()/2,var cy:Float = bitmap.height.toFloat()/2,var state:ImageClipperState = ImageClipperState()) {
        var rectPixels:LinkedList<Pixel> = LinkedList()
        var circlePixels:LinkedList<Pixel> = LinkedList()
        var rectPath = Path()
        var circlePath = Path()
        fun draw(canvas:Canvas,paint:Paint) {
            if(rectPixels.size == 0) {
                separatePixels()
            }
//            rectPixels.forEach { pixel ->
//                pixel.draw(canvas,paint)
//            }
            canvas.save()
            //canvas.clipPath(rectPath)
            canvas.drawBitmap(bitmap,0f,0f,paint)
            canvas.restore()
            canvas.save()
            canvas.translate(cx,cy)
            canvas.rotate(180f*state.scale)
            canvas.save()
            canvas.translate(-cx,-cy)
            canvas.clipPath(circlePath)
            canvas.drawBitmap(bitmap,0f,0f,paint)
            canvas.restore()
            canvas.restore()
        }
        private fun separatePixels() {
//            for(i in 0..bitmap.width-1) {
//                for(j in 0..bitmap.height-1) {
//                    val color = bitmap.getPixel(i,j)
//                    val pixel = Pixel(i.toFloat(),j.toFloat(),color)
//                    var distance = getDistance(i.toFloat(),j.toFloat())
//                    if(distance>size/2) {
//                        rectPixels.add(pixel)
//                        rectPath.addRect(pixel.rect,Path.Direction.CW)
//                    }
//                    else {
//                        circlePixels.add(pixel)
//                    }
//                }
//            }
            circlePath.addCircle(cx,cy,size/2,Path.Direction.CW)
        }
        private fun getDistance(x:Float,y:Float):Float {
            return Math.sqrt(Math.pow((cx-x).toDouble(),2.0)+Math.pow((cy-y).toDouble(),2.0)).toFloat()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x>=cx-size/2 && x<=cx+size/2 && y>=cy-size/2 && y<=cy+size/2
    }
    data class Pixel(var x:Float,var y:Float,var color:Int,var rect:RectF = RectF(x,y,x+1,y+1)) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = color
            canvas.drawRect(RectF(x,y,x+1,y+1),paint)
        }
    }
    data class ImageClipperState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update() {
            scale = Math.sin(deg*Math.PI/180).toFloat()
            deg += 4.5f
            if(deg > 180) {
                deg = 0f
            }
        }
        fun stopped():Boolean = deg == 0f
    }
    class ImageCircleClipperAnimator(var clipper:ImageCircleClipper,var view:ImageCircleClipperView,var animated:Boolean = false) {
        fun update() {
            if(animated) {
                clipper.update()
                if(clipper.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun draw(canvas: Canvas,paint:Paint) {
            clipper.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && clipper.handleTap(x,y)) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    class ImageCircleClipperRenderer(var view:ImageCircleClipperView,var time:Int = 0) {
        var animator:ImageCircleClipperAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width
                val h = canvas.height
                val bitmap = Bitmap.createScaledBitmap(view.bitmap,w,h,true)
                val size = Math.min(w,h).toFloat()/2
                animator = ImageCircleClipperAnimator(ImageCircleClipper(bitmap,size),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity,bitmap: Bitmap) {
            val view = ImageCircleClipperView(activity,bitmap)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}