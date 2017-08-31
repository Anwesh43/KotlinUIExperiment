package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 31/08/17.
 */
class BarPieLoaderView(ctx:Context,var n:Int = 4):View(ctx) {
    val renderer = BarPieLoaderRenderer()
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var onSelectionListener:BarPieLoaderOnSelectionListener?=null
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
    data class BarShape(var i:Int,var y:Float,var w:Float,var h:Float,var state:BarShapeState = BarShapeState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            var r = w/10
            canvas.save()
            canvas.translate(w/20,y+h/2)
            paint.style = Paint.Style.STROKE
            canvas.drawRoundRect(RectF(0f,-h/2,w*.9f,h/2),r,r,paint)
            canvas.scale(state.scale,1f)
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(RectF(0f,-h/2,w*.9f,h/2),r,r,paint)
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.w/20 && x<=this.w && y>=this.y && y<=this.y+h && state.dir == 0f
        fun stopped():Boolean = state.stopped()
    }
    data class BarShapeState(var scale:Float=0f,var dir:Float = 0f) {
        fun update() {
            scale+=dir*0.2f
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
        fun startUpdating() {
            dir = 1-2*scale
        }
    }
    data class PieShape(var x:Float,var y:Float,var r:Float,var deg:Float = 0f) {
        fun draw(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(0f,0f,r,paint)
            paint.style = Paint.Style.FILL
            canvas.drawArc(RectF(-r,-r,r,r),0f,deg,true,paint)
            canvas.restore()
        }
    }
    class BarPieLoaderAnimator(var pieShape:PieShape,var barShapes:ConcurrentLinkedQueue<BarShape>,var view:BarPieLoaderView) {
        var animated = false
        var tappedBars:ConcurrentLinkedQueue<BarShape> = ConcurrentLinkedQueue()
        fun draw(canvas:Canvas,paint:Paint) {
            pieShape.draw(canvas, paint)
            barShapes.forEach { barShape ->
                barShape.draw(canvas,paint)
            }
        }
        fun update() {
            if(animated) {
                tappedBars.forEach { bar ->
                    bar.update()
                    if(bar.stopped()) {
                        tappedBars.remove(bar)
                        when(bar.state.scale) {
                            0f -> {
                                view.onSelectionListener?.unselectListener?.invoke(bar.i)
                                pieShape.deg -= (360)/view.n
                            }
                            1f -> {
                                view.onSelectionListener?.selectListener?.invoke(bar.i)
                                pieShape.deg += (360)/view.n
                            }
                        }
                        if(tappedBars.size == 0) {
                            animated = false
                        }
                    }
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            barShapes.forEach { barShape ->
                if(barShape.handleTap(x,y)) {
                    barShape.startUpdating()
                    tappedBars.add(barShape)
                    if(tappedBars.size == 1) {
                        animated = true
                        view.postInvalidate()
                    }
                }
            }
        }
    }
    class BarPieLoaderRenderer {
        var time = 0
        var animator:BarPieLoaderAnimator?=null
        fun render(canvas:Canvas,paint:Paint,view:BarPieLoaderView) {
            if(time == 0) {
                paint.color = Color.parseColor("#0277BD")
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                paint.strokeWidth = w/40
                var barShapes:ConcurrentLinkedQueue<BarShape> = ConcurrentLinkedQueue()
                var gap = (0.8f*h)/(2*view.n+1)
                var pieShape = PieShape(w/2,h/10+gap/2,h/10)

                var y = 0.2f*h+3*gap/2
                for(i in 1..view.n) {
                    var barShape = BarShape(i,y,w,gap)
                    barShapes.add(barShape)
                    y += 2*gap
                }
                animator = BarPieLoaderAnimator(pieShape,barShapes,view)
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
        fun create(activity:Activity,vararg listeners:(Int)->Unit) {
            var view = BarPieLoaderView(activity)
            var size = DimensionsUtil.getDimension(activity)
            if(listeners.size == 2) {
                view.onSelectionListener = BarPieLoaderOnSelectionListener(listeners[0],listeners[1])
            }
            activity.addContentView(view,ViewGroup.LayoutParams(size.x,size.x))
        }
    }
    data class BarPieLoaderOnSelectionListener(var selectListener:(Int)->Unit,var unselectListener:(Int)->Unit)
}