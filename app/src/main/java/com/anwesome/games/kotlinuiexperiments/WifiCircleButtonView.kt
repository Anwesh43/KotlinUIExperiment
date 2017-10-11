package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.*
import android.widget.Toast

/**
 * Created by anweshmishra on 11/10/17.
 */
val colors = arrayOf(Color.parseColor("#1565C0"),Color.parseColor("#f44336"))
class WifiCircleButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = WifiCirlceButtonRenderer(this)
    var listener:WCBOnCollapseListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN ->{
                renderer.handleTap()
            }
        }
        return true
    }
    data class WifiCircleButton(var x:Float,var y:Float,var r:Float,var state:WifiCircleState = WifiCircleState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..1) {
                canvas.save()
                canvas.scale(1f-2*i,1f)
                for(j in 1..4) {
                    val newR = r/j
                    for(k in 0..1) {
                        paint.color = colors[k]
                        canvas.save()
                        canvas.rotate(-45+45f*(1-2*k)*state.scale)
                        canvas.drawPointArc(0f,0f,newR,0,90,paint)
                        canvas.restore()
                    }
                }
                canvas.restore()
            }
            canvas.restore()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class WifiCircleState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(scale > 1) {
                scale = 1f
                dir = 0f
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
    class WifiCircleAnimator(var wifiCircleButton:WifiCircleButton,var view:WifiCircleButtonView,var animated:Boolean = false) {
        fun draw(canvas:Canvas,paint:Paint) {
            wifiCircleButton.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                wifiCircleButton.update()
                if(wifiCircleButton.stopped()) {
                    animated = false
                    when(wifiCircleButton.state.scale) {
                        0f -> view.listener?.collapseListener?.invoke()
                        1f -> view.listener?.expandListener?.invoke()
                    }
                }
                try {
                    Thread.sleep(40)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                wifiCircleButton.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class WifiCirlceButtonRenderer(var view:WifiCircleButtonView,var time:Int = 0) {
        var animator:WifiCircleAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = WifiCircleAnimator(WifiCircleButton(w/2,h/2,Math.min(w,h)/3),view)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = Math.min(w,h)/60
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
        fun create(activity:Activity,vararg listeners:()->Unit) {
            val view = WifiCircleButtonView(activity)
            if(listeners.size == 2) {
                view.listener = WCBOnCollapseListener(listeners[0],listeners[1])
            }
            val size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
    data class WCBOnCollapseListener(var collapseListener:()->Unit,var expandListener:()->Unit)
}
fun Canvas.drawPointArc(x:Float,y:Float,r:Float,start:Int,end:Int,paint:Paint) {
    val path = Path()
    path.moveInCircle(x,y,r,start,end)
    this.drawPath(path,paint)
}
fun Path.moveInCircle(cx:Float,cy:Float,r:Float,start:Int,end:Int) {
    for(i in start..end) {
        val x = cx+r*Math.cos(i*Math.PI/180).toFloat()
        val y = cy+r*Math.sin(i*Math.PI/180).toFloat()
        if(i == start) {
            this.moveTo(x,y)
        }
        else {
            this.lineTo(x,y)
        }
    }
}