package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 05/12/17.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class RotateOverLineView(ctx:Context):View(ctx) {
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
    data class RotateOverLine(var x:Float,var y:Float,var size:Float,var deg:Float=0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.drawCircle(0f,0f,size/10,paint)
            canvas.save()
            canvas.rotate(deg)
            canvas.drawLine(0f,0f,size,0f,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            deg += 15f
            deg %= 360f
        }
    }
    data class RotateOverLineContainer(var w:Float,var h:Float) {
        var rotateOverLines:ConcurrentLinkedQueue<RotateOverLine> = ConcurrentLinkedQueue()
        var curr:RotateOverLine?=null
        fun addNewLine(x:Float,y:Float) {
            if(rotateOverLines.size == 0) {
                curr = RotateOverLine(x,y,Math.min(w,h)/9)
            }
            else {
                val x = curr?.x?:0f + Math.cos((curr?.deg?:0f)*Math.PI/180).toFloat()
                val y = curr?.y?:0f + Math.sin((curr?.deg?:0f)*Math.PI/180).toFloat()
                val deg = (((curr?.deg?:0f)-180)+360f)%360
                curr = RotateOverLine(x,y,deg)
            }
            rotateOverLines.add(curr)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            rotateOverLines.forEach { it ->
                it.draw(canvas,paint)
            }
            canvas.restore()
        }
        fun update() {
            curr?.update()
        }
    }
    data class RotateOverLineAnimator(var container:RotateOverLineContainer,var view:RotateOverLineView) {
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            container.update()
            try {
                Thread.sleep(50)
                view.invalidate()
            }
            catch(ex:Exception) {

            }
        }
        fun handleTap(x:Float,y:Float) {
            container.addNewLine(x,y)
        }
    }
    data class RotateOverLineRenderer(var time:Int = 0,var view:RotateOverLineView) {
        var animator:RotateOverLineAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                paint.color = Color.parseColor("#0D47A1")
                paint.strokeCap = Paint.Cap.ROUND
                paint.strokeWidth = Math.min(w,h)/60
                animator = RotateOverLineAnimator(RotateOverLineContainer(w, h), view)
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.handleTap(x,y)
        }
    }
}