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
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    //var crossTapView:CrossTapView?=null
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
        //PiePlusSectionView.create(this,{i->Toast.makeText(this,"$i is in",Toast.LENGTH_SHORT).show()},{i->Toast.makeText(this,"$i is out ",Toast.LENGTH_SHORT).show()})
       //RectCompArcView.create(this,{Toast.makeText(this,"On",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Off",Toast.LENGTH_SHORT).show()})
        //ChatHeadView.create(this)
       // DustbinFillButtonView.create(this)
        //AtomButtonView.create(this)
        //ChromeButtonView.create(this,{Toast.makeText(this,"Filled",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Emptied",Toast.LENGTH_SHORT).show()})
        //StepButtonView.create(this)
        //ColorRectBarView.create(this)
        //LineArcListView.create(this)
        //YTLogoButtonView.create(this)
        //RippleClickableView.create(this)
        //GridLineSquareView.create(this)
        //TriangleArrowButtonView.create(this)
        //PlayBarView.create(this,{Toast.makeText(this,"Filled",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Empty",Toast.LENGTH_LONG).show()})
        //PieLoaderDirectionView.create(this)
        //GtoBView.create(this)
        //TwoColorSwitchTriangleView.create(this,{Toast.makeText(this,"On",Toast.LENGTH_LONG)},{Toast.makeText(this,"Off",Toast.LENGTH_LONG)})
        //TwoColorRectView.create(this,{Toast.makeText(this,"1st color filled",Toast.LENGTH_LONG).show()},{Toast.makeText(this,"2nd color filled",Toast.LENGTH_LONG).show()})
        //BitmapExpanderView.create(this,bitmap,{Toast.makeText(this,"Expanded",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Collapsed",Toast.LENGTH_SHORT).show()})
        //ColorScreenRadioView.create(this,{Toast.makeText(this,"Filled",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"Emptied",Toast.LENGTH_SHORT).show()})
        //DoubleLineArcView.create(this,{i->Toast.makeText(this,"$i expanded",Toast.LENGTH_SHORT).show()},{i -> Toast.makeText(this,"$i collapsed",Toast.LENGTH_SHORT).show()})
        //SwiperSquareView.create(this)
       // TouchDownFillScreenView.create(this)
        //DoubleArrowTapMoverView.create(this)
        //DirectionPinView.create(this)
//        DirectionColoredArrowView.create(this,{i ->
//            Toast.makeText(this,"selected $i",Toast.LENGTH_SHORT).show()
//        })
//        val textView = TextView(this)
//        textView.textSize = 30f
//        var view = ClockTapView.create(this,{i -> textView.text = "${i}'O clock"})
//        view.x = 200f
//        view.y = 400f
//        textView.x = 230f
//        textView.y = 150f
//        addContentView(textView,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT))
        //PieColorBarView.create(this,{Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()})
       // RectBarExpanderView.create(this)
        //ImageCircleClipperView.create(this,bitmap,{Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()})
        //ColorPageSwiperView.create(this,arrayOf(Color.parseColor("#00BCD4"),Color.parseColor("#F4511E"),Color.parseColor("#f44336"),Color.parseColor("#303F9F"),Color.parseColor("#00838F"),Color.parseColor("#6A1B9A")),{index->Toast.makeText(this,"Now in page number ${index}",Toast.LENGTH_SHORT).show()})
        //MultiLineCircleView.create(this)
       // FillDownFourTriangleView.create(this,{Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()})
        //DirectionIndicatingArcView.create(this,{i -> Toast.makeText(this,"Selected ${i} arc",Toast.LENGTH_SHORT).show()})
        //PointedArrowView.create(this,{i->Toast.makeText(this,"${i} selected",Toast.LENGTH_SHORT).show()})
        //WifiCircleButtonView.create(this,{Toast.makeText(this,"collapsed",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"expanded",Toast.LENGTH_SHORT).show()})
        //DoubleTouchingTriangleView.create(this,{Toast.makeText(this,"opened",Toast.LENGTH_SHORT).show()},{Toast.makeText(this,"closed",Toast.LENGTH_SHORT).show()})
        //ArrowDirectionSquareCreatorView.create(this,{i->Toast.makeText(this,"${i+1} line is moving forward",Toast.LENGTH_SHORT).show()},{i->Toast.makeText(this,"${i+1} line is moving backward",Toast.LENGTH_SHORT).show()})
        //CenterToCornerBallView.create(this,{i->Toast.makeText(this,"Selected ${i}",Toast.LENGTH_SHORT).show()})
        //crossTapView = CrossTapView.crColoeate(this)
//        ColorBarButtonView.create(this)
//        ColorBarButtonView.addSelectionListener({i -> Toast.makeText(this,"$i expanded",Toast.LENGTH_SHORT).show()},{i -> Toast.makeText(this,"$i collapsed",Toast.LENGTH_SHORT).show()})
//         SideWiseLineView.create(this)
//         SideWiseLineView.addListener({i->
//             createToast("$i expanded")
//         },{i ->
//             createToast("$i collapsed")
//         })
//        LinkedBallButtonView.create(this)
//        LinkedBallButtonView.addSelectionListener({i->createToast("$i expanded")},{i->createToast("$i collapsed")})
//        CircularArrangedBallView.create(this)
//        CircularArrangedBallView.addSelectionListener({i->
//            createToast("$i collapsed")
//        },{i->
//            createToast("$i expanded")
//        })
//        ArrowTipRotatorView.create(this)
//        ArrowTipRotatorView.addListener({createToast("expanded")},{createToast("collapsed")})
//        FourCornerBallView.create(this)
//        FourCornerBallView.addListener({
//            createToast("clicked")
//        })
        //CorrespondingButtonPieView.create(this)
        //CorrespondingButtonPieView.addSelectionListener({i->createToast("$i collapsed")},{i->createToast("$i expanded")})
//        RotateLineButtonView.create(this)
//        RotateLineButtonView.addClickListener({i -> createToast("$i opened")},{i -> createToast("$i closed")})
//        RectEdgeView.create(this)
        RectEdgeRotatorView.create(this)
    }
    override fun onPause() {
        super.onPause()
        //crossTapView?.pause()
    }
    fun createToast(text:String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }
    override fun onResume(){
        super.onResume()
        //crossTapView?.resume()
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
