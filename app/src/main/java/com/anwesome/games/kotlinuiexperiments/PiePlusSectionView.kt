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
 * Created by anweshmishra on 03/09/17.
 */
class PiePlusSectionView(ctx:Context):View(ctx) {
    val renderer = PiePlusSectionRenderer()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var inOutListener:PPSOnOutInListener?=null
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
    data class PiePlusSection(var i:Int,var x:Float,var y:Float,var r:Float,var state:PieSectionState= PieSectionState()) {
        var lx = x - r + r*(i%2)
        var ly = y - r + r*(i/2)
        var ux = x + r*(i%2)
        var uy = y + r*(i/2)
        fun draw(canvas:Canvas,paint:Paint) {
            var index = i/2
            var deg = 180f + (1-2*index)*(90f*(i%2)+90f*(i/2))
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#1565C0")
            canvas.drawArc(RectF(-r,-r,r,r),(deg),90f*state.scale,true,paint)
            paint.color = Color.WHITE
            canvas.save()
            canvas.translate(-r/2+r*(i%2),-r/2+r*(i/2))
            canvas.rotate(45f*state.scale)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(-r/10,0f,r/10,0f,paint)
                canvas.restore()
            }
            canvas.restore()
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=lx && y>=ly && x<=ux && y<=uy && state.dir == 0
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopUpdating()
        fun startUpdating() {
            state.startUpdating()
        }
    }
    data class PieSectionState(var scale:Float = 0f,var dir:Int = 0) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0
            }
            if(scale < 0) {
                dir = 0
                scale = 0f
            }
        }
        fun stopUpdating():Boolean = dir == 0
        fun startUpdating() {
            dir = (1-2*scale).toInt()
        }
    }
    class PiePlusSectionAnimator(var piePlusSections:ConcurrentLinkedQueue<PiePlusSection>,var view:PiePlusSectionView) {
        var animated = false
        var tappedPiePlusSections:ConcurrentLinkedQueue<PiePlusSection> = ConcurrentLinkedQueue()
        fun draw(canvas: Canvas,paint:Paint) {
            paint.color = Color.parseColor("#1565C0")
            paint.style = Paint.Style.STROKE
            var r = 0.4f*Math.min(canvas.width.toFloat(),canvas.height.toFloat())
            paint.strokeWidth = r/40
            canvas.drawCircle(canvas.width.toFloat()/2,canvas.height.toFloat()/2,r,paint)
            paint.style = Paint.Style.FILL
            paint.strokeWidth = r/25
            piePlusSections.forEach { piePlusSection ->
                piePlusSection.draw(canvas,paint)
            }
        }
        fun update() {
            if(animated) {
                tappedPiePlusSections.forEach { tappedPiePlusSection ->
                    tappedPiePlusSection.update()
                    if(tappedPiePlusSection.stopped()) {
                        tappedPiePlusSections.remove(tappedPiePlusSection)
                        when(tappedPiePlusSection.state.scale) {
                            0f -> view.inOutListener?.closeListener?.invoke(tappedPiePlusSection.i)
                            1f -> view.inOutListener?.openListener?.invoke(tappedPiePlusSection.i)
                        }
                        if(tappedPiePlusSections.size == 0) {
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
            piePlusSections.forEach { piePlusSection ->
                if(piePlusSection.handleTap(x,y)) {
                    piePlusSection.startUpdating()
                    tappedPiePlusSections.add(piePlusSection)
                    if(tappedPiePlusSections.size == 1){
                        animated = true
                        view.postInvalidate()
                    }
                }
            }
        }
    }
    class PiePlusSectionRenderer {
        var animator:PiePlusSectionAnimator?=null
        var time = 0
        fun render(canvas:Canvas,paint:Paint,view:PiePlusSectionView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var r = 0.4f*Math.min(w,h)
                var piePlusSections:ConcurrentLinkedQueue<PiePlusSection> = ConcurrentLinkedQueue()
                for(i in 0..3) {
                    piePlusSections.add(PiePlusSection(i,w/2,h/2,r))
                }
                animator = PiePlusSectionAnimator(piePlusSections,view)
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
            var view = PiePlusSectionView(activity)
            if(listeners.size == 2) {
                view.inOutListener = PPSOnOutInListener(listeners[0],listeners[1])
            }
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
    data class PPSOnOutInListener(var openListener:(Int)->Unit,var closeListener:(Int)->Unit)
}