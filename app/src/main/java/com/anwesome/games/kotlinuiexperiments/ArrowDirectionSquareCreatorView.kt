package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import android.widget.Toast

/**
 * Created by anweshmishra on 13/10/17.
 */
class ArrowDirectionSquareCreatorView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = ArrowDirectionSquareRenderer(view=this)
    var movementListener:ArrowDirecMovementListener?=null
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
                canvas.rotate(90f*i*state.scales[i])
                canvas.drawLine(-size/2,-size/2,size/2,-size/2,paint)
                canvas.restore()
            }
            canvas.save()
            canvas.rotate(90f*state.j)
            canvas.drawLine(-size/2,-size/2,-size/2+size*state.scales[state.j],-size/2,paint)
            canvas.drawRotatedHorizontalTriangle(-size/2+size*state.scales[state.j],-size/2,90f+90*state.currDir,size/5,paint)
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
            }
            if(scales[j] < 0f) {
                scales[j] = 0f
                dir = 0f
            }
            if(dir == 0f) {
                if((j==0 && currDir == -1) || (j == 3 && currDir == 1)) {
                    currDir *= -1
                }
                else {
                    j += currDir
                }
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
                    when(creator.state.currDir) {
                        1 -> {
                            view.movementListener?.onpositivemove?.invoke(creator.state.j)
                        }
                        -1 -> {
                            view.movementListener?.onnegativemove?.invoke(creator.state.j)
                        }
                    }
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
                paint.color = Color.parseColor("#76FF03")
                paint.strokeWidth = Math.min(w,h)/50
                paint.strokeCap = Paint.Cap.ROUND
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
        fun create(activity:Activity,vararg listeners:(Int)->Unit) {
            val view = ArrowDirectionSquareCreatorView(activity)
            val size = DimensionsUtil.getDimension(activity)
            if(listeners.size == 2) {
                view.movementListener = ArrowDirecMovementListener(listeners[0],listeners[1])
            }
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
        }
    }
    data class ArrowDirecMovementListener(var onpositivemove:(Int)->Unit,var onnegativemove:(Int)->Unit)
}
