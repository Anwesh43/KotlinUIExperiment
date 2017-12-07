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
        }
        fun update(startcb:(Float,Int)->Unit) {

        }
        fun startUpdating(stopcb:()->Unit) {

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