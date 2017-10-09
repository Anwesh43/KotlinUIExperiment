package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 10/10/17.
 */
class PointedArrowView(ctx:Context):View(ctx) {
    val renderer = PointedArrowRenderer(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class PointedArrow(var x:Float,var y:Float,var size:Float,var state:PointedArrowState = PointedArrowState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            val j = state.j
            for(i in 0..j) {
                canvas.drawDirectedLine(size/2,i*90f,paint)
            }
            canvas.drawDirectedLine(size/2,j*90f+90f*state.scale,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
    }
    data class PointedArrowState(var scale:Float = 0f,var dir:Int = 0,var currDir:Int = 1,var j:Int = 0) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1 && currDir > 0) {
                dir = 0
                scale = 0f
                j++
                if(j == 3) {
                    currDir = -1
                    scale = 1f
                    j--
                }
            }
            else if(scale < 0 && currDir < 0) {
                dir = 0
                scale = 1f
                j--
                if(j == 0) {
                    currDir = 1
                    scale = 0f
                }
            }
        }
        fun startUpdating() {
            dir = currDir
        }
        fun stopped():Boolean = dir == 0
    }
    class PointedArrowAnimator(var pointedArrow:PointedArrow,var view:PointedArrowView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            pointedArrow.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                pointedArrow.update()
                if(pointedArrow.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(75)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                animated = true
                pointedArrow.startUpdating()
                view.postInvalidate()
            }
        }
    }
    class PointedArrowRenderer(var view:PointedArrowView,var time:Int = 0) {
        var animator:PointedArrowAnimator?=null
        fun handleTap() {
            animator?.handleTap()
        }
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = PointedArrowAnimator(PointedArrow(w/2,h/2,Math.min(w,h)/3),view)
                paint.strokeWidth = Math.min(w,h)/50
                paint.color = Color.parseColor("#673AB7")
                paint.strokeCap = Paint.Cap.ROUND
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
    }
    companion object {
        fun create(activity:Activity) {
            val view = PointedArrowView(activity)
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
}
fun Canvas.drawDirectedLine(size:Float,deg:Float,paint:Paint) {
    this.save()
    this.rotate(deg)
    this.drawLine(size,-size/2,size,-size/2,paint)
    val path = Path()
    path.addDownTriangle(size,size/2,size/10)
    this.drawPath(path,paint)
    this.restore()
}
fun Path.addDownTriangle(x:Float,y:Float,size:Float) {
    this.moveTo(x-size/2,y-size/2)
    this.lineTo(x+size/2,y-size/2)
    this.lineTo(x,y+size/2)
}