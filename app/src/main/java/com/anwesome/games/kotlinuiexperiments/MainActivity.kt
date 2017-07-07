package com.anwesome.games.kotlinuiexperiments

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var view:CircleCreatorView = CircleCreatorView(this)
//        val completionListener = CompletionListener(this)
        var view:SidewiseBallMoverView = SidewiseBallMoverView(this)
        //view.onCompletionListener = completionListener
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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