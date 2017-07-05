package com.anwesome.games.kotlinuiexperiments

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import CircleCreatorView
import android.graphics.Color
import android.view.WindowManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var view:CircleCreatorView = CircleCreatorView(this)
//        val completionListener = CompletionListener(this)
        var view:ColorPageView = ColorPageView(this)
        //view.onCompletionListener = completionListener
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        view.addColor(Color.parseColor("#E65100"))
        view.addColor(Color.parseColor("#00ACC1"))
        view.addColor(Color.parseColor("#f44336"))
        view.addColor(Color.parseColor("#C2185B"))
        setContentView(view)
    }
}
//data class CompletionListener(var activity: MainActivity):CircleCreatorView.OnCompletionListener {
//    override fun onCompleted(x: Float, y: Float) {
//        Toast.makeText(activity,"at $x and $y circle's animation is completed",Toast.LENGTH_SHORT).show()
//    }
//}
data class BallOutOfBoundListener(var activity: MainActivity):OnBallOutOfBoundListener {
    override fun onOutOfBound(index: Int) {
        Toast.makeText(activity,"$index number ball is out of index",Toast.LENGTH_SHORT).show()
    }
}