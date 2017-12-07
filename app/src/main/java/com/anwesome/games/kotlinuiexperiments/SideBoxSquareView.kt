package com.anwesome.games.kotlinuiexperiments
import android.graphics.*
import android.content.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 08/12/17.
 */
val sideBoxColors:Array<String> = arrayOf("#311B92","#004D40","#BF360C","#4CAF50","#FFC107","#00E676","#880E4F","#448AFF")
class SideBoxSquareView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class SideBoxSquare(var i:Int,var dx:Float,var y:Float,var size:Float,var x:Float=-dx+2*dx*(i%2),var px:Float = x) {
        val state = SideBoxSquareState()
        fun draw(canvas: Canvas,paint:Paint) {
            paint.color = Color.parseColor(sideBoxColors[i])
            x = px+(dx-px)
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class SideBoxSquareState(var scale:Float=0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale-prevScale)>1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
    data class SideBoxSquareContainer(var w:Float,var h:Float,var n:Int) {
        val state = SideBoxSquareContainerState(n)
        var squares:ConcurrentLinkedQueue<SideBoxSquare> = ConcurrentLinkedQueue()
        init {
            val size = Math.min(w,h)/3
            for(i in 0..n-1) {
                squares.add(SideBoxSquare(i,w/2,h/2,size))
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            squares.forEach { square ->
                square.draw(canvas,paint)
            }
            paint.color = Color.WHITE
            paint.strokeWidth = Math.min(w,h)/50
            paint.strokeCap = Paint.Cap.ROUND
            state.executeCb { j->
                if(n > 0) {
                    val scale = squares?.at(j)?.state?.scale ?: 0f
                    val gap = (8*w/10)/n
                    canvas.drawLine(w / 10, h / 10, w/10+gap*(j+scale), h / 10, paint)
                }
            }
        }
        fun update(stopcb:(Float,Int)->Unit) {
            state.executeCb { j->
                squares.at(j)?.update{scale->
                    stopcb(scale,j)
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            state.executeCb { j ->
                squares.at(j)?.startUpdating(startcb)
            }
        }
    }
    data class SideBoxSquareContainerState(var n:Int,var j:Int = 0,var dir:Int = 1) {
        fun incrementCounter() {
            j += dir
            if(j == n || j == -1) {
                dir *= -1
                j+=dir
            }
        }
        fun executeCb(cb:(Int)->Unit) {
            cb(j)
        }
    }
    data class SideBoxSquareAnimator(var container:SideBoxSquareContainer,var view:SideBoxSquareView) {
        var animated = false
        fun update() {
            if(animated) {
                container.update{scale,j->
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
        fun startUpdating() {
            if(!animated) {
                container.startUpdating{
                    animated = true
                    view.postInvalidate()
                }
            }
        }
    }
}
fun ConcurrentLinkedQueue<SideBoxSquareView.SideBoxSquare>.at(i:Int):SideBoxSquareView.SideBoxSquare? {
    var index = 0
    this.forEach {
        if(i == index) {
            return it
        }
        index++
    }
    return null
}