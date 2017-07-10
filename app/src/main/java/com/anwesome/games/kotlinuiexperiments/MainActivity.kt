package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var view:CircleCreatorView = CircleCreatorView(this)
//        val completionListener = CompletionListener(this)
//        var view:ColorExpanderRectView = ColorExpanderRectView(this)
//        //view.onCompletionListener = completionListener
//        view.onExpandListener = ExpandListener(this)
//        supportActionBar?.hide()
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        setContentView(view)
        setContentView(R.layout.activity_main)
        var viewGroup = BasicSwitchViewGroup(this)
        addContentView(viewGroup, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT))
        for(i in 1..8) {
            viewGroup.addSwitch(SwitchSelectionListener(this,i))
        }
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
class SwitchSelectionListener(var activity:MainActivity,var index:Int):OnSwitchSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"selected $index",Toast.LENGTH_SHORT).show()
    }

    override fun onUnSelect() {
        Toast.makeText(activity,"unselected $index",Toast.LENGTH_SHORT).show()
    }
}