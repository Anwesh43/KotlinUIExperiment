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
class CircularColorFilterImageView(var bitmap:Bitmap,ctx:Context):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer:CCFIVRenderer = CCFIVRenderer()
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
                drawingController = CCFIVDrawingController(ColorFilterImage(bitmap,w.toFloat()/2,h.toFloat()/2,r.toFloat()),v)
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
    data class ColorFilterImage(var bitmap:Bitmap,var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            var path:Path = Path()
            path.addCircle(0.0f,0.0f,r,Path.Direction.CW)
            canvas.clipPath(path)
            canvas.drawBitmap(bitmap,-r,-r,paint)
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
        fun create(activity:Activity,bitmap: Bitmap) {
            var dimension = getDimension(activity)
            activity.addContentView(CircularColorFilterImageView(bitmap,activity), ViewGroup.LayoutParams(dimension.x/2,dimension.x/2))
        }
        fun getDimension(activity: Activity):Point {
            var displayManager:DisplayManager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            var size = Point()
            var display:Display = displayManager.getDisplay(0)
            display?.getRealSize(size)
            return size
        }
    }
}