package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.view.View
import android.view.ViewGroup

/**
 * Created by anweshmishra on 10/07/17.
 */
class BasicSwitchViewGroup(ctx:Context):ViewGroup(ctx) {
    var w = 0
    var h = 0
    init {
        val displayManager:DisplayManager = ctx.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        var display = displayManager.getDisplay(0)
        var size = Point()
        display?.getRealSize(size)
        this.w = size.x
        this.h = size.y
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var hView = h/30
        var wView = 0
        for(i in 0..childCount-1) {
            var view = getChildAt(i)
            measureChild(view,widthMeasureSpec,heightMeasureSpec)
            hView+=view.measuredHeight
            wView = Math.max(wView,view.measuredWidth)
        }
        setMeasuredDimension(wView+wView/5,hView+w/15)
    }
    override fun onLayout(reloaded:Boolean,a:Int,b:Int,wv:Int,hv:Int) {
        var y = h/30
        var x = w/30
        for(i in 0..childCount-1) {
            var child = getChildAt(i)
            child.layout(x,y,x+child.measuredWidth,y+child.measuredHeight)
            y += child.measuredHeight
        }
    }
}