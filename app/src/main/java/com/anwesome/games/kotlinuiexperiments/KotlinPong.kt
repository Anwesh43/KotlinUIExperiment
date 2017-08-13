package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 13/08/17.
 */

class KotlinPongView(ctx:Context):View(ctx) {
    val pongRenderer = PongsRenderer()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        pongRenderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                pongRenderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class DimensionHolder(var w:Float,var h:Float){
        fun decidePongDirection(pong:Pong) {
            var r = pong.r
            if(pong.x<r) {
                pong.dirx = 1.0f
            }
            if(pong.x>w-r) {
                pong.dirx = -1.0f
            }
            if(pong.y<r) {
                pong.diry = 1.0f
            }
            if(pong.y>h-r) {
                pong.diry = -1.0f
            }

        }
        fun createPong(x:Float,y:Float):Pong {
            var pong = Pong(x,y,Math.min(w,h)/20,0.0f,0.0f)
            if(x>=w/2) {
                pong.dirx = 1.0f
            }
            if(x<w/2) {
                pong.dirx = -1.0f
            }
            if(y>=h/2) {
                pong.diry = 1.0f
            }
            if(y<h/2) {
                pong.diry = -1.0f
            }
            return pong
        }
    }
    data class Pong(var x:Float,var y:Float,var r:Float,var dirx:Float,var diry:Float) {
        fun draw(canvas: Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.argb(150,244,67,54)
            canvas.drawCircle(0.0f,0.0f,r,paint)
            canvas.restore()
        }
        fun update(dimensionHolder:DimensionHolder) {
            x+=dirx*20
            y+=diry*20
            dimensionHolder.decidePongDirection(this)
        }
        fun changeDir() {
            dirx *= -1
            diry *= -1
        }
    }
    data class PongRenderController(var dimensionHolder: DimensionHolder,var pongView: KotlinPongView,var pongs:ConcurrentLinkedQueue<Pong> = ConcurrentLinkedQueue()) {
        fun handleTap(x:Float,y:Float) {
            pongs.add(dimensionHolder.createPong(x,y))
        }
        fun draw(canvas:Canvas,paint:Paint) {
            pongs.forEach { pong->
                pong.draw(canvas,paint)
            }
        }
        private fun animateView() {
            try {
                Thread.sleep(75)
                pongView.invalidate()
            }
            catch(ex:Exception) {

            }
        }
        fun update() {
            pongs.forEach { pong->
                pong.update(dimensionHolder)
            }
            animateView()
        }
    }
    class PongsRenderer(var time:Int = 0) {
        var controller:PongRenderController?=null
        fun render(canvas:Canvas,paint:Paint,v:KotlinPongView) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var dimensionHolder = DimensionHolder(w,h)
                controller = PongRenderController(dimensionHolder,v)
            }
            controller?.draw(canvas,paint)
            time++
            controller?.update()
        }
        fun handleTap(x:Float,y:Float) {
            controller?.handleTap(x,y)
        }
    }
    companion object {
        fun create(activity: Activity) {
            var pongView = KotlinPongView(activity)
            activity.setContentView(pongView)
        }
    }
}