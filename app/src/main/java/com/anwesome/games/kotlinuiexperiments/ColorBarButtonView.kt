package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.graphics.*
import android.view.*
/**
 * Created by anweshmishra on 16/10/17.
 */
val barColors:Array<Int> = arrayOf(Color.parseColor("#f44336"),Color.parseColor("#673AB7"),Color.parseColor("#00695C"))
class ColorBarButtonView(ctx:Context):View(ctx) {
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
    data class ColorScreen(var i:Int,var w:Float,var h:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = color
            canvas.save()
            canvas.translate(0f,h/10)
            canvas.drawRect(RectF(0f,0f,w,9*h/10),paint)
            canvas.restore()
        }
        fun update() {

        }
        fun stopped():Boolean = false
        fun startUpdating() {

        }
    }
    data class ColorCircleButton(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float,color:Int) {
            canvas.drawColorScaledCircle(x,y,r,scale,color,paint)
        }
    }
}
fun Canvas.drawColorScaledCircle(x:Float,y:Float,r:Float,scale:Float,color:Int,paint:Paint) {
    this.save()
    this.translate(x,y)
    paint.color = Color.GRAY
    this.drawCircle(0f,0f,r,paint)
    this.save()
    this.scale(scale,scale)
    paint.color = color
    this.drawCircle(0f,0f,r,paint)
    this.restore()
    this.restore()
}