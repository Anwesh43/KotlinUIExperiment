package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*

/**
 * Created by anweshmishra on 13/10/17.
 */
class ArrowDirectionSquareCreatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ArrowDirectionSquareRenderer(view=this)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        if(event.action == MotionEvent.ACTION_DOWN) {
            renderer.handleTap()
        }
        return true
    }
    data class ArrowDirectionSquareCreator(var x:Float,var y:Float,var size:Float,var state:ArrowDirectionSquareCreatorState = ArrowDirectionSquareCreatorState()) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            for(i in 0..state.j-1) {
                canvas.save()
                canvas.rotate(-90f*i*state.scales[i])
                canvas.drawLine(-size/2,-size/2,size/2,-size/2,paint)
                canvas.restore()
            }
            canvas.save()
            canvas.rotate(90f*state.j)
            canvas.drawLine(-size/2,-size/2,-size/2+size*state.scales[state.j],-size/2,paint)
            canvas.drawRotatedHorizontalTriangle(-size/2,-size/2+size,0f,size/25,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            state.update()
        }
        fun startUpdating() {
            state.startUpdating()
        }
        fun stopped():Boolean = state.stopped()
    }
    data class ArrowDirectionSquareCreatorState(var dir:Float=0f,var j:Int = 0,var currDir:Int = 1) {
        var scales:Array<Float> = arrayOf(0f,0f,0f,0f)
        fun update() {
            scales[j] += dir*0.1f
            if(scales[j]>1) {
                scales[j] = 1f
                dir = 0f
                j+=currDir
            }
            if(scales[j] < 0f) {
                scales[j] = 0f
                dir = 0f
                j+=currDir
            }
            if(dir == 0f && (j==-1 || j == 4)) {
                currDir *= -1
                j+=currDir
            }
        }
        fun startUpdating() {
            dir = currDir.toFloat()
        }
        fun stopped():Boolean = dir == 0f
    }
    class ArrowDirectionSqaureAnimator(var creator:ArrowDirectionSquareCreator,var view:ArrowDirectionSquareCreatorView) {
        var animated = false
        fun update() {
            if(animated) {
                creator.update()
                if(creator.stopped()) {
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
        fun draw(canvas:Canvas,paint:Paint) {
            creator.draw(canvas,paint)
        }
        fun startUpdating() {
            if(!animated) {
                creator.startUpdating()
                animated = true
                view.postInvalidate()
            }
        }
    }
    class ArrowDirectionSquareRenderer(var time:Int = 0,var view:ArrowDirectionSquareCreatorView) {
        var animator:ArrowDirectionSqaureAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = ArrowDirectionSqaureAnimator(ArrowDirectionSquareCreator(w/2,h/2,Math.min(w,h)/2),view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap() {
            animator?.startUpdating()
        }
    }
}
