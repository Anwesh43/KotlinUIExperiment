package com.anwesome.games.kotlinuiexperiments

import android.view.View
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent

/**
 * Created by anweshmishra on 11/07/17.
 */
class ImageColorFilterView(ctx:Context,var bitmap:Bitmap,var color:Int=Color.BLUE):View(ctx) {
    val paint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {
        
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return true
    }
}