package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 06/09/17.
 */
class DustbinFillButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DustbinRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class DustbinFill(var w:Float,var h:Float,var state:DustbinState= DustbinState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.parseColor("#9E9E9E")
            drawBin(canvas, paint,Color.parseColor("#757575"))
            paint.color = Color.parseColor("#1565C0")
            canvas.save()
            var clipPath = Path()
            clipPath.addRect(RectF(w/10,7*h/10+h/30-(6*h/10+2*h/30)*this.state.scale,7*w/10,7*h/10+h/30),Path.Direction.CCW)
            canvas.clipPath(clipPath)
            drawBin(canvas,paint,Color.parseColor("#0D47A1"))
            canvas.restore()
        }
        private fun drawBin(canvas: Canvas,paint: Paint,color:Int) {
            var path = Path()
            path.moveTo(w/5,7*h/10)
            path.lineTo(w/10,h/10)
            path.lineTo(7*w/10,h/10)
            path.lineTo(6*w/10,7*h/10)
            path.lineTo(w/5,7*h/10)
            canvas.drawPath(path,paint)
            drawEllipse(canvas,paint,(w/5+6*w/10)/2,7*h/10,(0.6f*w-0.2f*w)/2,h/30)
            paint.color = color
            drawEllipse(canvas,paint,(w/10+7*w/10)/2,h/10,(3*w)/10,h/30)
        }
        private fun drawEllipse(canvas: Canvas,paint:Paint,x1:Float,y1:Float,r1:Float,r2:Float) {
            canvas.drawArc(RectF(x1-r1,y1-r2,x1+r1,y1+r2),0f,360f,true,paint)
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdate()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class DustbinState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir * 0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdate() {
            dir = 1f-2*scale
        }
    }
    class DustbinAnimator(var dustbin:DustbinFill,var view:DustbinFillButtonView) {
        var animated = false
        fun update() {
            if(animated) {
                dustbin.update()
                if(dustbin.stopped()) {
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
        fun draw(canvas: Canvas,paint:Paint) {
            dustbin.draw(canvas,paint)
        }
        fun handleTap() {
            if(!animated) {
                dustbin.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class DustbinRenderer {
        var time = 0
        var animator:DustbinAnimator?=null
        fun render(canvas: Canvas,paint:Paint,view: DustbinFillButtonView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var dustbinFill = DustbinFill(w,h)
                animator = DustbinAnimator(dustbinFill,view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.handleTap()
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = DustbinFillButtonView(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
}