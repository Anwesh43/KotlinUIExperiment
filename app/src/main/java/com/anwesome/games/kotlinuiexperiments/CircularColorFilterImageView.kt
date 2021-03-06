package com.anwesome.games.kotlinuiexperiments
import android.app.Activity
import android.graphics.*
import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 19/07/17.
 */
class CircularColorFilterImageView(var bitmap:Bitmap,ctx:Context,var color:Int):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer:CCFIVRenderer = CCFIVRenderer()
    var selectionListener:CCFIVSelectionListener?=null
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint,this)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    class CCFIVRenderer {
        var time = 0
        var drawingController:CCFIVDrawingController?=null
        fun render(canvas:Canvas,paint:Paint,v:CircularColorFilterImageView) {
            if(time == 0) {
                var w = canvas.width
                var h = canvas.height
                var r = Math.min(w,h)/2
                var bitmap = Bitmap.createScaledBitmap(v.bitmap,w,h,true)
                drawingController = CCFIVDrawingController(ColorFilterImage(bitmap,w.toFloat()/2,h.toFloat()/2,r.toFloat(),v.color),v)
            }
            drawingController?.draw(canvas,paint)
            drawingController?.animate()
            time++
        }
        fun handleTap() {
            drawingController?.handleTap()
        }
    }
    class CCFIVStateController {
        var scale = 0.0f
        var dir = 0
        fun update() {
            scale += 0.1f*dir
            if(scale > 1) {
                dir = 0
                scale = 1.0f
            }
            if(scale < 0) {
                dir = 0
                scale = 0.0f
            }
        }
        fun startUpdating() {
            dir = when(scale) {
                0.0f -> 1
                1.0f -> -1
                else -> dir
            }
        }
        fun stopped() = dir == 0
    }
    data class ColorFilterImage(var bitmap:Bitmap,var x:Float,var y:Float,var r:Float,var color:Int) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            var path:Path = Path()
            path.addCircle(0.0f,0.0f,r,Path.Direction.CW)
            canvas.clipPath(path)
            paint.color = Color.BLACK
            canvas.drawBitmap(bitmap,-r,-r,paint)
            paint.color = Color.argb(150,Color.red(color),Color.green(color),Color.blue(color))
            canvas.drawArc(RectF(-r,-r,r,r),-90.0f,360*scale,true,paint)
            canvas.restore()
        }
    }
    class CCFIVDrawingController(var colorFilterImage:ColorFilterImage,var v:CircularColorFilterImageView) {
        var animated = false
        var stateController = CCFIVStateController()
        fun draw(canvas:Canvas,paint:Paint) {
            colorFilterImage.draw(canvas,paint,stateController.scale)
        }
        fun animate() {
            if(animated) {
                stateController.update()
                if(stateController.stopped()) {
                    animated = false
                    if(stateController.scale >= 1) {
                        v.selectionListener?.onSelect()
                    }
                    else {
                        v.selectionListener?.onUnSelect()
                    }
                }
                try {
                    Thread.sleep(75)
                    v.invalidate()
                }
                catch (ex:Exception) {

                }
            }
        }
        fun handleTap() {
            if(!animated) {
                stateController.startUpdating()
                animated = true
                v.postInvalidate()
            }
        }
    }
    companion object {
        fun create(activity:Activity,bitmap: Bitmap,color:Int=Color.parseColor("#FF5722"),vararg listeners:CCFIVSelectionListener) {
            var dimension = getDimension(activity)
            var view = CircularColorFilterImageView(bitmap,activity,color)
            when(listeners.size) {
                1->{
                    view.selectionListener = listeners[0]
                }
            }
            activity.addContentView(view, ViewGroup.LayoutParams(dimension.x/2,dimension.x/2))
        }
        private fun getDimension(activity: Activity):Point {
            var displayManager:DisplayManager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            var size = Point()
            var display:Display = displayManager.getDisplay(0)
            display?.getRealSize(size)
            return size
        }
    }
    interface CCFIVSelectionListener {
        fun onSelect() {

        }
        fun onUnSelect() {

        }
    }
}