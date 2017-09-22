package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 22/09/17.
 */
class BitmapExpanderView(ctx:Context,var bitmap:Bitmap):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = BitmapExpanderRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
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
    data class BitmapExpander(var w:Float,var h:Float,var bitmap:Bitmap,var cx:Float = w/2,var cy:Float = h/20,var state:BitmapExpanderState = BitmapExpanderState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(cx,cy+(h/5)*state.scale)
            canvas.rotate(180f*state.scale)
            paint.style = Paint.Style.STROKE
            val path = Path()
            path.moveTo(-w/20,w/20)
            path.lineTo(0f,0f)
            path.lineTo(w/20,w/20)
            canvas.drawPath(path,paint)
            canvas.restore()
            canvas.drawBitmap(bitmap,0f,-h/5+(h/5)*state.scale,paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
        fun handleTap(x:Float,y:Float):Boolean = x >= this.cx - w/20 && x <= this.cx + w/20 && y>=(cy+(h/5)*state.scale)-w/20 && y<=(cy+(h/5)*state.scale)+w/20
    }
    data class BitmapExpanderState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0f
                scale = 1f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    class BitmapExpanderAnimator(var expander:BitmapExpander,var view:BitmapExpanderView,var animated:Boolean  = false) {
        fun update() {
            if(animated) {
                expander.update()
                if(expander.stopped()) {
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
        fun draw(canvas:Canvas,paint:Paint) {
            expander.draw(canvas,paint)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && expander.handleTap(x,y)) {
                animated = true
                expander.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class BitmapExpanderRenderer {
        var time = 0
        var bitmapExpanderAnimator:BitmapExpanderAnimator?=null
        fun render(canvas:Canvas,paint:Paint,view:BitmapExpanderView) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                val bitmap = Bitmap.createScaledBitmap(view.bitmap,w.toInt(),(h/5).toInt(),true)
                bitmapExpanderAnimator = BitmapExpanderAnimator(BitmapExpander(w,h,bitmap),view)
                paint.strokeWidth = w/40
                paint.strokeCap = Paint.Cap.ROUND
                paint.color = Color.WHITE
            }
            bitmapExpanderAnimator?.draw(canvas,paint)
            bitmapExpanderAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            bitmapExpanderAnimator?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity,bitmap: Bitmap) {
            var view = BitmapExpanderView(activity,bitmap)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x,size.y))
        }
    }
}