import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import java.util.*

class CircleCreatorView(ctx:Context):View(ctx) {
    var animationHandler:AnimationHandler?=null
    var time:Int = 0
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        if(time == 0) {
            animationHandler = AnimationHandler(this)
        }
        val w = canvas.width
        val h = canvas.height
        time++
        canvas.drawColor(Color.parseColor("#212121"))
        animationHandler?.draw_animate(canvas,paint,Math.min(w,h)*1.0f)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                animationHandler?.add_circle(event.x,event.y)
            }
        }
        return true
    }
    class Circle {
        var x:Float = 0.0f
        var y:Float = 0.0f
        var deg:Float = 0.0f
        constructor(x:Float,y:Float) {
            this.x = x
            this.y = y
        }
        fun draw(canvas:Canvas,paint:Paint,r:Float) {
            paint.color = Color.argb(100,2,119,189)
            canvas.save()
            canvas.translate(x,y)
            var path:Path = Path()
            path.moveTo(0.0f,0.0f)
            var i = 0.0f
            while(i<=deg) {
                val x:Double = r*Math.cos(i*Math.PI/180)
                val y:Double = r*Math.sin(i*Math.PI/180)
                path.lineTo(x.toFloat(),y.toFloat())
                i+=10
            }
            canvas.drawPath(path,paint)
            canvas.restore()
        }
        fun update() {
            deg += 20.0f
        }
        fun stopped():Boolean = deg == 0.0f
        override fun hashCode(): Int = x.toInt()+y.toInt()
    }
}
class AnimationHandler {
    val circles:LinkedList<CircleCreatorView.Circle> = LinkedList()
    var animated:Boolean = false
    var v:View?=null
    constructor(v:View) {
        this.v = v
    }
    fun draw_animate(canvas:Canvas,paint:Paint,size:Float) {
        if(animated) {
            circles.forEach { circle ->
                circle.draw(canvas, paint, size / 20)
                circle.update()
                if (circle.stopped()) {
                    circles.remove(circle)
                    if(circles.size == 0) {
                        animated = false
                    }
                }
            }
            try {
                Thread.sleep(50)
                v.invalidate()
            }
            catch (ex:Exception) {

            }
        }
    }
    fun add_circle(x:Float,y:Float) {
        circles.add(CircleCreatorView.Circle(x,y))
        if(circles.size == 1 && !animated) {
            animated = true
            v.postInvalidate()
        }
    }
}
