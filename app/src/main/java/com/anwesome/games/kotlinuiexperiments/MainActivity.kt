package com.anwesome.games.kotlinuiexperiments

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        setContentView(view)
//
//        setContentView(R.layout.activity_main)
//        var viewGroup = BasicSwitchViewGroup(this)
//        addContentView(viewGroup, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT))
//        for(i in 1..8) {
//            viewGroup.addSwitch(SwitchSelectionListener(this,i))
//        }
        var bitmap:Bitmap = BitmapFactory.decodeResource(resources,R.drawable.stp)
//        var view:ScaleHorizontalButtonListView = ScaleHorizontalButtonListView(this)
//        for(i in 0..5) {
//            view.addButton()
//        }
//        view.onClickListener = HorizontalScaleButtonClickListener(this)
       // view.onSelectionListener = ImageSelectionListener(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //CircularColorFilterImageView.create(this,bitmap,Color.parseColor("#0097A7"),CircularColorImageListener(this))
        //BiDirecLoaderButtonView.create(this)
        //VerticalCollapButton.create(this,ExpandCollapListener(this))
//        DotLineSwitchView.create(this,500,500)
//        HorizontalCollapButtonView.create(this,HCBExpandCollapListener(this))
        //PieSwitchListView.create(this)
        //ArrowMoverView.create(this)
        //PausePlayButton.create(this)
        //DoubleSideArcButton.create(this)
      //  IClassButton.create(this)
       // ScaleUpColorFilterImageView.create(this,bitmap)
        //RollerButton.create(this)
        //TapStarView.create(this)
        //BarButtonView.create(this)
        //LockerView.create(this)
        //PyramidView.create(this)
        //GreenToRedButtonView.create(this)
        //TickLineButtonView.create(this)
        //SettingButton.create(this,SettingButtonClickListener(this))
        //DoubleArrowLineButton.create(this)
        //SquareCornerSwitch.create(this)
        //TickBoxSwitchView.create(this)
        //KotlinPongView.create(this)
        //SwappableCircleView.create(this,6)
        //SwappableCircleView.addSwapListener(SwapListener(this))
        //StackButton.create(this,Color.parseColor("#AD1457"),"Hello")
//        StackButtonListView.create(this)
//        var colorTextMap = mapOf<String,Int>(Pair("Hello",Color.RED),Pair("Hi",Color.BLUE),Pair("Me",Color.CYAN),Pair("None",Color.MAGENTA),Pair("More",Color.parseColor("#BF360C")),Pair("See",Color.parseColor("#f44336")),Pair("Eat",Color.parseColor("#69F0AE")))
//        colorTextMap.keys.forEach { text->
//            StackButtonListView.addButton(colorTextMap.get(text)?:0,text,CloseListener(this,text))
//        }
//        StackButtonListView.show(this)
       // SearchButtonView.create(this,SearchButtonOpenCloseListener(this))
//        BallUpView.create(this)
//        BallUpView.addListener(BallUpListener(this))
      //  BallCircleLayoutView.create(this)
        //RightUpBallView.create(this)
        //BoxPieLoaderView.create(this)
//        BoxPieLoaderList.create(this)
//        for(i in 0..12) {
//            BoxPieLoaderList.addView(BoxPieSelectionListener(this,i))
//        }
//        BoxPieLoaderList.show(this)
//        RectEdgePieBallView.create(this)
//        RectEdgePieBallView.addSelectionListener(REBSelectionListener(this))
       // CircularPlayRectView.create(this)
        //PieCornerBallMoverView.create(this,PieCornerSelectionListener(this))
       // PiePolygonalView.create(this,6)
        //PieLineDotView.create(this,PLDSelectionListener(this))
        //MultiCircularButtonView.create(this,{Toast.makeText(this,"1",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"2",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"3",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"4",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"5",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"6",Toast.LENGTH_SHORT).show()})
        //BiDirecDotView.create(this,{Toast.makeText(this,"Opened",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Closed",Toast.LENGTH_SHORT).show()})
        //FourColorTriangleView.create(this,{Toast.makeText(this,"Opened",Toast.LENGTH_LONG).show()},{Toast.makeText(this,"Closed",Toast.LENGTH_LONG).show()})
        //ArrowPieMoverView.create(this,{Toast.makeText(this,"Opened",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Closed",Toast.LENGTH_SHORT).show()})
        //BarPieLoaderView.create(this,{i->Toast.makeText(this,"$i selected",Toast.LENGTH_SHORT).show()},{i->Toast.makeText(this,"$i unselected",Toast.LENGTH_SHORT).show()})
        //SideSquarePieView.create(this,{i->Toast.makeText(this,"$i opened",Toast.LENGTH_SHORT).show()},{i->Toast.makeText(this,"$i closed",Toast.LENGTH_SHORT).show()})
        PiePlusSectionView.create(this,{i->Toast.makeText(this,"$i is in",Toast.LENGTH_SHORT).show()},{i->Toast.makeText(this,"$i is out ",Toast.LENGTH_SHORT).show()})
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
class ImageSelectionListener(var activity: MainActivity):OnImageSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"selected",Toast.LENGTH_SHORT).show()
    }

    override fun onUnselect() {
        Toast.makeText(activity,"unselected",Toast.LENGTH_SHORT).show()
    }
}
class SettingButtonClickListener(var activity: MainActivity):SettingButton.SBClickListener {
    override fun onClick() {
        Toast.makeText(activity,"Clicked",Toast.LENGTH_SHORT).show()
    }
}
class HorizontalScaleButtonClickListener(var activity: MainActivity):OnSHBClickListener {
    override fun onClick(index: Int) {
        Toast.makeText(activity,"$index clicked",Toast.LENGTH_SHORT).show();
    }
}
class ExpandCollapseListener(var activity:MainActivity):OnExpandCollapseListener{
    override fun onCollapse() {
        Toast.makeText(activity,"collapsed",Toast.LENGTH_SHORT).show()
    }
    override fun onExpand() {
        Toast.makeText(activity,"expanded",Toast.LENGTH_SHORT).show()
    }
}
class CustomDownloadListener(var activity: MainActivity):DownloadButtonListener {
    override fun onInstallIndicator() {
        Toast.makeText(activity,"Installed",Toast.LENGTH_SHORT).show()
    }
    override fun onUnInstallIndicator() {
        Toast.makeText(activity,"UnInstalled",Toast.LENGTH_SHORT).show();
    }
}
class CircularColorImageListener(var activity:MainActivity):CircularColorFilterImageView.CCFIVSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"Selected",Toast.LENGTH_SHORT).show()
    }

    override fun onUnSelect() {
        Toast.makeText(activity,"UnSelected",Toast.LENGTH_SHORT).show()
    }
}
class ExpandCollapListener(var activity:MainActivity):OnExpandCollapseListener {
    override fun onExpand() {
        Toast.makeText(activity,"Expanded",Toast.LENGTH_SHORT).show()
    }
    override fun onCollapse() {
        Toast.makeText(activity,"Collapsed",Toast.LENGTH_SHORT).show()
    }
}
class HCBExpandCollapListener(var activity:MainActivity):HorizontalCollapButtonView.HSBOnExpandCollapseListener {
    override fun onExpand() {
        makeToast("Expanded")
    }
    private fun makeToast(text:String) {
        Toast.makeText(activity,text,Toast.LENGTH_SHORT).show()
    }
    override fun onCollapse() {
        makeToast("Collapsed")
    }
}
class CloseListener(var activity: MainActivity,var text:String):OnButtonCloseListener {
    override fun onClose() {
        Toast.makeText(activity,"Closed $text",Toast.LENGTH_SHORT).show()
    }
}
class SwapListener(var activity: MainActivity):SwappableCircleView.OnSwapListener {
    override fun onSwap(firstIndex: Int, secondIndex: Int) {
        Toast.makeText(activity,"$firstIndex swapped with $secondIndex",Toast.LENGTH_SHORT).show()
    }
}
class SearchButtonOpenCloseListener(var activity: MainActivity):SearchButtonView.OnSearchButtonOpenCloseListener {
    override fun onOpen() {
        Toast.makeText(activity,"Opened",Toast.LENGTH_SHORT).show()
    }
    override fun onClose() {
        Toast.makeText(activity,"Closed",Toast.LENGTH_SHORT).show()
    }
}
class BallUpListener(var activity:MainActivity):BallUpView.OnBallUpListener {
    override fun onBallUp(index:Int) {
        Toast.makeText(activity,"ball $index is up",Toast.LENGTH_SHORT).show()
    }
}
class REBSelectionListener(var activity: MainActivity):RectEdgePieBallView.REBOnSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"Selected",Toast.LENGTH_SHORT).show()
    }
    override fun onUnSelect() {
        Toast.makeText(activity,"Unselected",Toast.LENGTH_SHORT).show()
    }
}
class BoxPieSelectionListener(var activity:MainActivity,var i:Int):BoxPieLoaderOnSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"Selected $i",Toast.LENGTH_SHORT).show()
    }

    override fun onUnSelect() {
        Toast.makeText(activity,"UnSelected $i",Toast.LENGTH_SHORT).show()
    }
}
class PieCornerSelectionListener(var activity: MainActivity):PieCornerBallMoverView.PieCornerOnSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"Selected",Toast.LENGTH_SHORT).show()
    }

    override fun onUnSelect() {
        Toast.makeText(activity,"UnSelected",Toast.LENGTH_SHORT).show()
    }
}
class PLDSelectionListener(var activity:MainActivity):PieLineDotView.PLDOnSelectionListener {
    override fun onSelect() {
        Toast.makeText(activity,"selected",Toast.LENGTH_SHORT).show()
    }
    override fun onUnSelect() {
        Toast.makeText(activity,"unselected",Toast.LENGTH_SHORT).show()
    }
}