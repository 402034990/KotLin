package com.example.kotlin_demo.view.custom_view

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Scroller
import com.example.administrator.kotlin.FlowIndicator

import com.example.myapplication.utils.ImageLoader

import java.util.Timer
import java.util.TimerTask


/**
 * 图片轮播
 * @author yao
 */
class SlideAutoLoopView(internal var mContext: Context, attrs: AttributeSet) : ViewPager(mContext, attrs) {
    /** 自动播放的标识符 */
    internal val ACTION_PLAY = 1
    /**定义FlowIndicator:图片指示器view */
    internal var mFlowIndicator: FlowIndicator = FlowIndicator(mContext)
    /** 轮播图片的适配器 */
    internal var mAdapter: SlideAutoLoopAdapter? = null
    /** 图片数量 */
    internal var mCount: Int = 0
    /** 图片轮播间隔时间 */
    internal var mDuration = 2000
    /** 相册的图片下载地址数组 */
    internal var mAlbumImgUrl: Array<String>? = null
    internal var mTimer: Timer? = null
    internal var mHandler: Handler = Handler()
    internal var mAutoSwitch = false

    init {
        initHandler()
        setListener()
    }

    private fun setListener() {
        setOnPageChangeListener()
        setOnTouchListener()
    }

    /**
     * 设置触摸页面的事件监听
     */
    private fun setOnTouchListener() {
        this.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                mAutoSwitch = false
            }
            false
        }
    }

    /**
     * 监听ViewPager页面改变
     */
    private fun setOnPageChangeListener() {
        this.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                //设置指示器中实心圆的切换
                mFlowIndicator.setmFocus(position % mCount)
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                // TODO Auto-generated method stub

            }

            override fun onPageScrollStateChanged(arg0: Int) {
                // TODO Auto-generated method stub

            }
        })
    }

    private fun initHandler() {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == ACTION_PLAY) {//若是播放操作
                    if (!mAutoSwitch) {//若不是自动播放状态
                        mAutoSwitch = true//设置为自动播放状态
                    } else {//设置为下一个item
                        //取出当前item的下标
                        var currentItem = this@SlideAutoLoopView.currentItem
                        currentItem++//递增
                        //设置当前item为下一个
                        this@SlideAutoLoopView.currentItem = currentItem
                    }
                }
            }
        }
    }

    /**
     * 轮播图片的适配器
     * @author
     */
    internal inner class SlideAutoLoopAdapter() : PagerAdapter() {
        var context: Context? = null
        var albumImgUrl: Array<String>? = null
        var count: Int? = null
        constructor(context: Context, albumImgUrl: Array<String>, count: Int) : this() {
            this.context = context
            this.albumImgUrl = albumImgUrl
            this.count = count
        }
        override fun getCount(): Int {
            if (count == 0) {
                return 0
            }
            return Integer.MAX_VALUE
        }


        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {

            return arg0 === arg1
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val iv = ImageView(context)
            val params = ViewPager.LayoutParams()
            iv.layoutParams = params
            container.addView(iv)
            val imgUrl = albumImgUrl!![position%count!!]
            ImageLoader.downloadImg(context!!, iv, imgUrl)
            return iv
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    /**
     * 开始图片的轮播
     */
    fun startPlayLoop(flowIndicator: FlowIndicator, albumImgUrl: Array<String>, count: Int) {
        if (mAdapter == null) {
            mCount = count
            this.mFlowIndicator = flowIndicator
            mFlowIndicator.setmCount(count)
            mFlowIndicator.setmFocus(0)
            this.mAlbumImgUrl = albumImgUrl
            mAdapter = SlideAutoLoopAdapter(mContext, mAlbumImgUrl!!,count)
            this.adapter = mAdapter

            try {
                val field = ViewPager::class.java.getDeclaredField("mScroller")
                field.isAccessible = true
                val scroller = MyScroller(mContext, LinearInterpolator())
                scroller.duration = 500
                scroller.startScroll(0, 0, 50, 0)
                field.set(this, scroller)
            } catch (e: NoSuchFieldException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        if (mTimer == null) {
            mTimer = Timer()
        }
        /*mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(ACTION_PLAY);
            }
        }, 1000,mDuration);
        */
        val timer = MyTimer()
        mTimer!!.scheduleAtFixedRate(timer, 500, mDuration.toLong())
    }

    /**
     * 停止图片轮播
     */
    fun stopPlayLoop() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
    }

    /**
     * ViewPager列表项滚动的距离、时间间隔的设置
     * @author yao
     */
    internal inner class MyScroller(context: Context, interpolator: Interpolator)// TODO Auto-generated constructor stub
        : Scroller(context, interpolator) {

        var duration: Int? = null//图片移动的时间间隔

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int,
                                 duration: Int) {
            super.startScroll(startX, startY, dx, dy, this.duration!!)
        }
    }

    internal inner class MyTimer : TimerTask() {

        override fun run() {
            mHandler.sendEmptyMessage(ACTION_PLAY)
        }
    }

}
