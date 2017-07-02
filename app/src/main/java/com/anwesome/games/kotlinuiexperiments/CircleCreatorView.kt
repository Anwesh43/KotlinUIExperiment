import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

class CircleCreatorView(ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        val w = canvas.width
        val h = canvas.height

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {

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
