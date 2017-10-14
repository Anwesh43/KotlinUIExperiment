package com.anwesome.games.kotlinuiexperiments
import android.view.*
import android.content.*
import android.graphics.*
/**
 * Created by anweshmishra on 14/10/17.
 */
class CenterToCornerBallView(ctx:Context):View(ctx) {
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
    data class CornerBall(var i:Float,var w:Float,var h:Float,var x:Float = 0f,var y:Float = 0f,var r:Float = Math.min(w,h)/20) {
        init {
            x = (i%2)*(w-2*r)+r
            y = (i/2)*(h-2*r)+r
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.strokeWidth = r/10
            paint.color = Color.parseColor("#FF9800")
            canvas.save()
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(x,y,r,paint)
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CenterBall(var r:Float,var x:Float=0f,var y:Float=0f,var wx:Float = 0f,var wy:Float = 0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#1A237E")
            canvas.save()
            canvas.translate(x,y)
            canvas.scale(1f,1f)
            canvas.drawArc(RectF(-r,-r,r,r),0f,360f,true,paint)
            canvas.restore()
        }
        fun update(){
            x+=wx
            y+=wy
        }
        fun setDiff(x:Float,y:Float) {
            wx = x
            wy = y
        }
        fun startUpdating() {

        }
        fun stopped():Boolean {
            return true
        }
    }
    data class CenterToBallState(var dir:Float = 0f,var j:Int = 0) {
        var scales:Array<Float> = arrayOf(0f,0f,0f)
        fun update() {
            if(j < scales.size) {
                scales[j]+=dir*0.1f
                if(scales[j] > 1) {
                    if(j != 1) {
                        dir = 0f
                    }
                    scales[j] = 1f
                    j++
                }
            }
        }
        fun stopped():Boolean = dir == 0f
        fun startUpdating() {
            if(dir == 0f) {
                dir = 1f
            }
        }
    }
}