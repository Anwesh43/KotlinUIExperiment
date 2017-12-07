package com.anwesome.games.kotlinuiexperiments
import android.graphics.*
import android.content.*
import android.view.*
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
    data class SideBoxSquare(var i:Int,var dx:Float,var y:Float,var size:Float,var x:Float=(2*(i%2)-1)*dx,var px:Float = x) {
        fun draw(canvas: Canvas,paint:Paint) {
            paint.color = Color.parseColor(sideBoxColors[i])
            x = px+(dx-px)
            canvas.save()
            canvas.translate(x,y)
            canvas.drawRoundRect(RectF(-size/2,-size/2,size/2,size/2),size/10,size/10,paint)
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

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
        }
    }
}