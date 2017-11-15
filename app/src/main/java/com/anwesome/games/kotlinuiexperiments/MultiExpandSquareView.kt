package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 15/11/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class MultiExpandSquareView(ctx:Context,var n:Int = 10):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = MutliSquareRenderer(this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean  {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class MultiExpandSquare(var i:Int,var n:Int,var x:Float,var y:Float,var size:Float,var cx:Float = x,var cy:Float = y) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            val gap = (((n/2-i).toFloat()/n.toFloat())*size/4)
            x = cx - gap
            y = cy - gap
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
        }
    }
    data class MutliExpandSquareState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update() {
            scale += dir*0.1f
            if(Math.abs(scale-prevScale) > 1) {
                dir = 0f
                scale = (prevScale+1)%2
                prevScale = scale
            }
        }
        fun startUpdating() {
            dir = 1-2*this.prevScale
        }
        fun stopped():Boolean = dir == 0f
    }
    class MultiExpanderSquareContainer(var w:Float,var h:Float,var n:Int) {
        var state = MutliExpandSquareState()
        var squares:ConcurrentLinkedQueue<MultiExpandSquare> = ConcurrentLinkedQueue()
        init {
            for(i in 0..n-1) {
                squares.add(MultiExpandSquare(i,n,w/2,h/2,Math.min(w,h)/3))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            squares.forEach { square ->
                square.draw(canvas,paint,state.scale)
            }
        }
        fun update(stopcb:()->Unit) {
            state.update()
            if(state.stopped()) {
                stopcb()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating()
            startcb()
        }
    }
    class MultiSquareAnimator(var multiSquareContainer:MultiExpanderSquareContainer,var view:MultiExpandSquareView) {
        var animated:Boolean = false
        fun update() {
            if(animated) {
                try {
                    multiSquareContainer.update {
                        animated = false
                    }
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex: Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            multiSquareContainer.draw(canvas,paint)
        }
        fun startUpdating() {
            if(!animated) {
                multiSquareContainer.startUpdating {
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
    class MutliSquareRenderer(var view:MultiExpandSquareView,var time:Int = 0) {
        var animator:MultiSquareAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = MultiSquareAnimator(MultiExpanderSquareContainer(w,h,view.n),view)
                paint.color = Color.parseColor("#283593")
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.startUpdating()
        }
    }
    companion object{
        fun create(activity:Activity):View {
            val view = MultiExpandSquareView(activity)
            activity.setContentView(view)
            return view
        }
    }
}