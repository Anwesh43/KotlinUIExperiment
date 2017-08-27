package com.anwesome.games.kotlinuiexperiments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 27/08/17.
 */
class MultiCircularButtonView(ctx:Context,var n:Int = 6):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val controller = MultiCircularButtonController(this)
    var listeners:ArrayList<()->Unit> = ArrayList()
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.argb(0,0,0,0))
        controller.render(canvas,paint)
    }
    fun addButton(listener:()->Unit) {
        listeners.add(listener)
        n = listeners.size
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                controller.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class ControlButton(var x:Float,var y:Float,var r:Float,var scale:Float=0f) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45*scale)
            paint.color = Color.parseColor("#304FFE")
            canvas.drawCircle(0f,0f,r,paint)
            paint.color = Color.WHITE
            paint.strokeWidth = r/10
            paint.strokeCap = Paint.Cap.ROUND
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(90f*i)
                canvas.drawLine(0f,-2*r/3,0f,2*r/3,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            this.scale = scale
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class CircularButton(var x:Float,var y:Float,var r:Float,var scale:Float = 0f,var deg:Float = 0f) {
        var clickListener:CircularButtonClickListener?=null
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.parseColor("#304FFE")
            canvas.drawCircle(0f,0f,r,paint)
            canvas.save()
            canvas.scale(scale,scale)
            paint.color = Color.argb(130,255,255,255)
            canvas.drawCircle(0f,0f,r,paint)
            canvas.restore()
            canvas.restore()
        }
        fun update() {
            deg+=18f
            scale = Math.sin(deg*Math.PI/180).toFloat()
            if(deg > 180) {
                deg = 0f
                scale = 0f
                clickListener?.listener?.invoke()
            }
        }
        fun stopAnimating():Boolean = deg == 0f
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class MultiCircularButton(var cx:Float,var cy:Float,var finalR:Float,var n:Int,var listeners:ArrayList<()->Unit>,var r:Float=0f,var gapDeg:Float = 360f/n) {
        var circularButtons:ConcurrentLinkedQueue<CircularButton> = ConcurrentLinkedQueue()
        var tappedButtons:ConcurrentLinkedQueue<CircularButton> = ConcurrentLinkedQueue()
        init {
            var gapDeg = 360f/n
            var r1 = r/5
            if(listeners.size > 0) {
                n = listeners.size
            }
            for(i in 0..n-1) {
                var deg = i*gapDeg
                var x = cx+(r-r1)*Math.cos(deg*Math.PI/180).toFloat()
                var y = cy+(r-r1)*Math.sin(deg*Math.PI/180).toFloat()
                var circularButton = CircularButton(x,y,finalR/5)
                circularButtons.add(circularButton)
                if(i < listeners.size) {
                    circularButton.clickListener = CircularButtonClickListener(listeners[i])
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            circularButtons.forEach{ circularButton ->
                circularButton.draw(canvas,paint)
            }
        }
        fun update(scale:Float) {
            r = finalR*scale
            var r1 = r/5
            var deg = 0f
            circularButtons.forEach { circularButton ->
                circularButton.x = cx+(r-r1)*Math.cos(deg*Math.PI/180).toFloat()
                circularButton.y = cy+(r-r1)*Math.sin(deg*Math.PI/180).toFloat()
                deg+=gapDeg
            }
        }
        fun updateTappedButon() {
            tappedButtons.forEach { button ->
                button.update()
                if(button.stopAnimating()) {
                    tappedButtons.remove(button)
                }
            }
        }
        fun handleTap(x:Float,y:Float):Boolean {
            circularButtons.forEach { cb ->
                if(cb.handleTap(x,y)) {
                    tappedButtons.add(cb)
                    return true
                }
            }
            return false
        }
    }
    class MultiCircularButtonAnimator(var controller: MultiCircularButtonController):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var anim = ValueAnimator.ofFloat(0f,1f)
        var reverseAnim = ValueAnimator.ofFloat(1f,0f)
        var animated:Boolean = false
        var mode:Int = 0
        init {
            anim.addUpdateListener(this)
            reverseAnim.addUpdateListener(this)
            anim.addListener(this)
            reverseAnim.addListener(this)
            anim.duration = 500
            reverseAnim.duration = 500
        }
        override fun onAnimationUpdate(animator:ValueAnimator) {
            if(animated) {
                controller.update(animator.animatedValue as Float)
            }
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                mode = (mode+1)%2
                animated = false
            }
        }
        fun start() {
            if(!animated) {
                when(mode) {
                    0 -> anim.start()
                    1 -> reverseAnim.start()
                }
                animated = true
            }
        }
    }
    class ButtonTapAnimator(var view:MultiCircularButtonView,var multiCircularButton: MultiCircularButton?) {
        var isUpdating:Boolean = false
        fun update() {
            if(isUpdating) {
                multiCircularButton?.updateTappedButon()
                if((multiCircularButton?.tappedButtons?.size?:0) == 0) {
                    isUpdating = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            if(multiCircularButton?.handleTap(x,y)?:false) {
                if((multiCircularButton?.tappedButtons?.size?:0) == 1) {
                    isUpdating = true
                    view.postInvalidate()
                }
            }
        }
    }
    class MultiCircularButtonController(var view:MultiCircularButtonView) {
        var rendered = 0
        var controlButton:ControlButton?=null
        var multiCircularButton:MultiCircularButton?=null
        var mcbAnimator:MultiCircularButtonAnimator = MultiCircularButtonAnimator(this)
        var tapButtonAnimator:ButtonTapAnimator?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(rendered == 0) {
                create(canvas.width.toFloat(),canvas.height.toFloat())
            }
            multiCircularButton?.draw(canvas,paint)
            controlButton?.draw(canvas,paint)
            tapButtonAnimator?.update()
            rendered++
        }
        fun create(w:Float,h:Float) {
            multiCircularButton = MultiCircularButton(w/2,h/2,Math.min(w,h)/2,view.n,view.listeners)
            controlButton = ControlButton(w/2,h/2,Math.min(w,h)/10)
            tapButtonAnimator = ButtonTapAnimator(view,multiCircularButton)
        }
        fun update(scale:Float) {
            multiCircularButton?.update(scale)
            controlButton?.update(scale)
            view.postInvalidate()
        }
        fun handleTap(x:Float,y:Float) {
            if(controlButton?.handleTap(x,y)?:false) {
                mcbAnimator.start()
            }
            else {
                tapButtonAnimator?.handleTap(x,y)
            }
        }
    }
    companion object {
        fun create(activity:Activity,vararg listeners:()->Unit) {
            var dimension:Point = DimensionsUtil.getDimension(activity)
            var w = dimension.x/2
            var view = MultiCircularButtonView(activity)
            listeners.forEach { listener->
                view.addButton(listener)
            }
            activity.addContentView(view, ViewGroup.LayoutParams(w,w))
        }
    }
    data class CircularButtonClickListener(var listener:()->Unit)
}