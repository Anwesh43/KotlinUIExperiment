package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 07/12/17.
 */
import android.view.*
import android.content.*
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.*

class DirecArrowPieView(ctx:Context):View(ctx) {
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
    data class DirecArrowPie(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            val r = Math.min(w,h)/5
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*scale,false,paint)
            canvas.restore()
            paint.style = Paint.Style.FILL
            val size = Math.min(w,h)/10
            for(i in 0..1) {
                canvas.save()
                canvas.translate(w/2,h/5+h/2*i)
                canvas.rotate(180f*scale)
                for(j in 0..1) {
                    canvas.save()
                    canvas.scale(1f-2*j,1f)
                    val path = Path()
                    path.moveTo(0f, -size / 2)
                    path.lineTo(size, 0f)
                    path.lineTo(0f, size / 2)
                    canvas.drawPath(path, paint)
                    canvas.restore()
                }
                canvas.restore()

            }
        }
    }
    data class DirecArowwPieContainer(var w:Float,var h:Float) {
        var pie = DirecArrowPie(w,h)
        val state = DirecArowwPieContainerState()
        fun draw(canvas:Canvas,paint:Paint) {
            pie.draw(canvas,paint,state.scale)
        }
        fun update(stopcb:()->Unit) {
            state.update(stopcb)
        }
    }
    data class DirecArowwPieContainerState(var scale:Float = 0f,var deg:Float = 0f) {
        fun update(stopcb:()->Unit) {
            deg += 10f
            scale = Math.sin(deg*Math.PI/180).toFloat()
            if(deg > 180) {
                deg = 0f
                scale = 1f
            }
        }
    }
}

