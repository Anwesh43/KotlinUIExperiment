package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 09/08/17.
 */
class SettingButton(ctx:Context):View(ctx) {
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
    data class SettingButtonShape(var x:Float,var y:Float,var r:Float,var deg:Float=0.0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(deg)
            drawPath(canvas,paint,r)
            canvas.restore()
        }
        private fun drawPath(canvas:Canvas,paint:Paint,r:Float) {
            var path = Path()
            paint.color = Color.GRAY
            for(i in 0..11) {
                var currdeg = i*30.0f
                var r1 = 2*r/3
                var x1 = r1*Math.cos(currdeg*Math.PI/180).toFloat()
                var y1 = r1*Math.sin(currdeg*Math.PI/180).toFloat()
                var x2 = r*Math.cos((currdeg+5)*Math.PI/180).toFloat()
                var y2 = r*Math.sin((currdeg+5)*Math.PI/180).toFloat()
                var x3 = r*Math.cos((currdeg+25)*Math.PI/180).toFloat()
                var y3 = r*Math.sin((currdeg+25)*Math.PI/180).toFloat()
                var x4 = r1*Math.cos((currdeg+30)*Math.PI/180).toFloat()
                var y4 = r1*Math.sin((currdeg+30)*Math.PI/180).toFloat()
                if(i == 0) {
                    path.moveTo(x1,y1)
                }
                else {
                    path.lineTo(x1,y1)
                }
                path.lineTo(x2,y2)
                path.lineTo(x3,y3)
                path.lineTo(x4,y4)
            }
            canvas.drawPath(path,paint)
        }
        fun update(scale:Float) {
            deg = 360.0f*scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x -r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    class SBState(var deg:Float = 0.0f,var scale:Float = 0.0f) {
        fun update() {
            deg += 4.5f
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            if(deg > 180) {
                deg = 0.0f
            }
        }
        fun stopped():Boolean = deg == 0.0f
    }
    
}