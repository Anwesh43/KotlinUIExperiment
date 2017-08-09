package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 09/08/17.
 */
class SettingButton(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = SBRenderer()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
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
            for(i in 0..7) {
                var currdeg = i*45.0f
                var r1 = 2*r/3
                var x1 = r1*Math.cos((currdeg+5)*Math.PI/180).toFloat()
                var y1 = r1*Math.sin((currdeg+5)*Math.PI/180).toFloat()
                var x2 = r*Math.cos((currdeg)*Math.PI/180).toFloat()
                var y2 = r*Math.sin((currdeg)*Math.PI/180).toFloat()
                var x3 = r*Math.cos((currdeg+30)*Math.PI/180).toFloat()
                var y3 = r*Math.sin((currdeg+30)*Math.PI/180).toFloat()
                var x4 = r1*Math.cos((currdeg+25)*Math.PI/180).toFloat()
                var y4 = r1*Math.sin((currdeg+25)*Math.PI/180).toFloat()
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
            deg = 180.0f*scale
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
    class SBAnimController(var sb:SettingButtonShape,var v:SettingButton,var animated:Boolean = false,var state:SBState=SBState()) {
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                sb.update(state.scale)
                try {
                    Thread.sleep(75)
                    v.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && sb.handleTap(x,y)) {
                animated = true
                v.postInvalidate()
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            sb.draw(canvas,paint)
        }
    }
    class SBRenderer {
        var time = 0
        var controller:SBAnimController?=null
        fun render(canvas:Canvas,paint:Paint,v:SettingButton) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var sb = SettingButtonShape(w/2,h/2,Math.min(w,h)/3)
                controller = SBAnimController(sb,v)
            }
            controller?.draw(canvas,paint)
            controller?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity:Activity) {
            var view = SettingButton(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/3,size.x/3))
        }
    }
}