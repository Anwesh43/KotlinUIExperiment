package com.anwesome.games.kotlinuiexperiments
import android.content.*
import android.graphics.*
import android.view.*
import java.util.concurrent.ConcurrentLinkedQueue

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
    data class ColorScreen(var i:Int,var w:Float,var h:Float,var color:Int,var state:ColorBarState = ColorBarState()) {
        var colorCircleButton = ColorCircleButton(w/2-h/10+i*h/10,h/20,h/20,color)
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = color
            canvas.save()
            canvas.translate(0f,h/10)
            canvas.drawRect(RectF(0f,0f,w,(9*h/10)*state.scale),paint)
            canvas.restore()
            colorCircleButton.draw(canvas,paint,0f)
        }
        fun update() {
            state.update()
        }
        fun stopped():Boolean = state.stopped()
        fun startUpdating() {
            state.startUpdating()
        }
        fun handleTap(x:Float,y:Float):Boolean = colorCircleButton.handleTap(x,y)
    }
    data class ColorCircleButton(var x:Float,var y:Float,var r:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.drawColorScaledCircle(x,y,r,scale,color,paint)
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y-r && y<=this.y+r
    }
    data class ColorBarState(var scale:Float = 0f,var dir:Float = 0f) {
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                scale = 1f
                dir = 0f
            }
            if(scale < 0) {
                scale = 0f
                dir = 0f
            }
        }
        fun startUpdating() {
            dir = 1-2*scale
        }
        fun stopped():Boolean = dir == 0f
    }
    data class ColorBarScreenContainer(var w:Float,var h:Float) {
        var screens:ConcurrentLinkedQueue<ColorScreen> = ConcurrentLinkedQueue()
        var updatingScreens:ConcurrentLinkedQueue<ColorScreen> = ConcurrentLinkedQueue()
        init {
            var i = 0
            barColors.forEach { color ->
                screens.add(ColorScreen(i,w,h,color))
                i++
            }
        }
        fun update(stopcb:()->Unit) {
            updatingScreens.forEach { screen ->
                screen.update()
                if(screen.stopped()) {
                    screens.remove(screen)
                    if(screens.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            screens.forEach { screen ->
                screen.draw(canvas,paint)
            }
        }
        fun handleTap(x:Float,y:Float,startcb:()->Unit) {
            screens.forEach { screen ->
                if(screen.handleTap(x,y)) {
                    screen.startUpdating()
                    updatingScreens.add(screen)
                    if(screens.size == 1) {
                        startcb()
                    }
                    return
                }
            }
        }
    }
    class ColorScreenAnimator(var container:ColorBarScreenContainer,var view:ColorBarButtonView,var animated:Boolean = false) {
        fun draw(canvas: Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
        fun update() {
            if(animated) {
                container.update({
                    animated = false
                })
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun handleTap(x:Float,y:Float) {
            container.handleTap(x,y,{
                animated = true
                view.postInvalidate()
            })
        }
    }
    class ColorBarRenderer(var view:ColorBarButtonView,var time:Int = 0) {
        var colorScreenAnimator:ColorScreenAnimator?=null
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                var w = canvas.width.toFloat()
                var h = canvas.height.toFloat()
                colorScreenAnimator = ColorScreenAnimator(ColorBarScreenContainer(w,h),view)
            }
            colorScreenAnimator?.draw(canvas,paint)
            colorScreenAnimator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            colorScreenAnimator?.handleTap(x,y)
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