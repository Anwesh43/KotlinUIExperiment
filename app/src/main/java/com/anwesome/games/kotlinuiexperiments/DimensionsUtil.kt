package com.anwesome.games.kotlinuiexperiments

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.view.Display

/**
 * Created by anweshmishra on 21/07/17.
 */
class DimensionsUtil {
    companion object {
        fun getDimension(activity: Activity):Point {
            var displayManager: DisplayManager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            var display: Display = displayManager.getDisplay(0)
            var point = Point()
            display.getRealSize(point)
            return point
        }
    }
}