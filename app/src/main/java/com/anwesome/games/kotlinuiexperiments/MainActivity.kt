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
        var view:ColorExpanderRectView = ColorExpanderRectView(this)
        //view.onCompletionListener = completionListener
        view.onExpandListener = ExpandListener(this)
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
class ExpandListener(var activity: MainActivity):ColorExpanderRectView.OnExpandListener {
    override fun onExpand() {
        Toast.makeText(activity,"Expanded",Toast.LENGTH_SHORT).show()
    }
    override fun onShrink() {
        Toast.makeText(activity,"Shrinked",Toast.LENGTH_SHORT).show()
    }
}