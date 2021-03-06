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
 * Created by anweshmishra on 10/08/17.
 */
class DoubleArrowLineButton(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = DALBRenderer()
    override fun onDraw(canvas:Canvas) {
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
    data class ArrowLine(var x:Float,var y:Float,var w:Float) {
        fun draw(canvas: Canvas,paint:Paint,scale:Float) {
            canvas.drawLine(x,y,x+w*scale,y,paint)
        }
    }
    data class DoubleArrow(var x:Float,var y:Float,var size:Float,var w:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x+w*scale,y)
            canvas.rotate(180.0f*scale)
            for(i in 0..1) {
                canvas.save()
                canvas.translate(size/2*i,0.0f)
                var path = Path()
                path.moveTo(0.0f,0.0f)
                path.lineTo(-size/2,-size/2)
                path.lineTo(-size/2,size/2)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-2*size && x<=this.x+2*size && y>=this.y-2*size && y<=this.y+2*size
    }
    data class DoubleLineArrow(var doubleArrow:DoubleArrow,var arrowLine:ArrowLine) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            doubleArrow.draw(canvas,paint,scale)
            arrowLine.draw(canvas,paint,scale)
        }
        fun handleTap(x:Float,y:Float):Boolean = doubleArrow.handleTap(x,y)
    }
    class DALBState {
        var scale = 0.0f
        var deg = 0.0f
        fun update() {
            deg += 0.9f
            scale = Math.abs(Math.sin(deg*Math.PI/180)).toFloat()
            if(deg > 180.0f) {
                deg = 0.0f
            }
        }
        fun stopped():Boolean = deg == 0.0f
    }
    class DALBRenderController(var dla:DoubleLineArrow,var v:DoubleArrowLineButton,var state:DALBState = DALBState(),var animated:Boolean = false) {
        fun update() {
            if(animated) {
                state.update()
                if(state.stopped()) {
                    animated = false
                }
                try {
                    Thread.sleep(2)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            dla.draw(canvas,paint,state.scale)
        }
        fun handleTap(x:Float,y:Float) {
            if(!animated && dla.handleTap(x,y)) {
                animated = true
                v.postInvalidate()
            }
        }
    }
    class DALBRenderer {
        var time = 0
        var controller:DALBRenderController?=null
        fun render(canvas:Canvas,paint:Paint,v:DoubleArrowLineButton) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                var arrowSize = w/10
                var doubleArrow = DoubleArrow(arrowSize/2,h/2,arrowSize,w-arrowSize/2)
                var arrowLine = ArrowLine(0.0F,h/2+h/3,w-arrowSize)
                var doubleArrowLine = DoubleLineArrow(doubleArrow,arrowLine)
                controller = DALBRenderController(doubleArrowLine,v)
                paint.color = Color.parseColor("#0D47A1")
                paint.strokeWidth = h/15
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
            var view = DoubleArrowLineButton(activity)
            var size = DimensionsUtil.getDimension(activity)
            activity.addContentView(view, ViewGroup.LayoutParams(size.x/3,size.y/10))
        }
    }
}