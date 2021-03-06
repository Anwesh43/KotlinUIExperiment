package com.anwesome.games.kotlinuiexperiments

/**
 * Created by anweshmishra on 07/12/17.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*

class DirecArrowPieView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DirecArrowPieRenderer(this)
    var clickListener:DirecArrowPieClickListener?=null
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.startUpdating()
            }
        }
        return true
    }
    fun addClickListener(onClickListener:()->Unit) {
        clickListener = DirecArrowPieClickListener(onClickListener)
    }
    data class DirecArrowPie(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(w/2,h/2)
            paint.style = Paint.Style.STROKE
            val r = Math.min(w,h)/5
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,r,paint)
            paint.color = Color.parseColor("#4A148C")
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f*scale,false,paint)
            canvas.restore()
            paint.style = Paint.Style.FILL
            val size = Math.min(w,h)/10
            paint.color = Color.GRAY
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
            deg += 3f
            scale = Math.sin(deg*Math.PI/180).toFloat()
            if(deg > 180) {
                deg = 0f
                scale = 0f
                stopcb()
            }
        }
    }
    data class DirecArrowPieAnimator(var container:DirecArowwPieContainer,var view:DirecArrowPieView) {
        var animated = false
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update {
                    animated = false
                    view.clickListener?.clickListener?.invoke()
                }
                try {
                    Thread.sleep(20)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun startUpdating() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class DirecArrowPieRenderer(var view:DirecArrowPieView,var time:Int = 0) {
        var animator:DirecArrowPieAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = DirecArrowPieAnimator(DirecArowwPieContainer(w,h),view)
                paint.strokeWidth = Math.min(w,h)/50
            }
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun startUpdating() {
            animator?.startUpdating()
        }
    }
    companion object {
        fun create(activity:Activity):DirecArrowPieView {
            val view = DirecArrowPieView(activity)
            activity.setContentView(view)
            return view
        }
    }
    data class DirecArrowPieClickListener(var clickListener:()->Unit)
}

