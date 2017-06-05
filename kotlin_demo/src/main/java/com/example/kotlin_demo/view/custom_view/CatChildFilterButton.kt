package com.example.kotlin_demo.view.custom_view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.administrator.kotlin.bean.CategoryChildBean
import com.example.administrator.kotlin.utils.Utils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.adapter.CatFilterAdapter

import java.util.ArrayList


/**
 * Created by Administrator on 2017/5/9 0009.
 */

class CatChildFilterButton(internal var mContext: Context, attrs: AttributeSet) : Button(mContext, attrs) {
    internal var mbtnTop: CatChildFilterButton = this
    internal var mPopupWindow: PopupWindow?=null
    internal var mgvCategory: GridView = GridView(mContext)
    internal var mAdapter: CatFilterAdapter? = null

    /**
     * true:arrow down
     * false:arrow up
     */
    internal var mExpandOff: Boolean = false

    init {
        mExpandOff = true
        initGridView()
    }

    private fun initPopupWindow() {
        mPopupWindow = PopupWindow()
        mPopupWindow?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopupWindow?.height = LinearLayout.LayoutParams.WRAP_CONTENT
        mPopupWindow?.isTouchable = true
        mPopupWindow?.isOutsideTouchable = false
        mPopupWindow?.setBackgroundDrawable(ColorDrawable(0xbb000000.toInt()))
        mPopupWindow?.contentView = mgvCategory
        mPopupWindow?.showAsDropDown(mbtnTop)
    }

    private fun initGridView() {
        mgvCategory.columnWidth = Utils.px2dp(mContext, 1500)
        mgvCategory.horizontalSpacing = Utils.px2dp(mContext, 10)
        mgvCategory.verticalSpacing = Utils.px2dp(mContext, 10)
        mgvCategory.numColumns = 2
        mgvCategory.setBackgroundColor(Color.TRANSPARENT)
        mgvCategory.setPadding(3, 3, 3, 3)
        mgvCategory.cacheColorHint = 0
    }

    private fun setBtnTopArrow() {
        var right: Drawable
        val resId: Int
        if (mExpandOff) {
            right = mContext.resources.getDrawable(R.drawable.arrow2_down)
            resId = R.drawable.arrow2_down
        } else {
            right = mContext.resources.getDrawable(R.drawable.arrow2_up)
            resId = R.drawable.arrow2_up
        }
        right.setBounds(0, 0, getDrawableWidth(mContext, resId), getDrawableHeight(mContext, resId))
        mbtnTop.setCompoundDrawables(null, null, right, null)
        mExpandOff = !mExpandOff
    }


    /**
     * 设置分类列表的下拉按钮单击事件监听

     * @param groupName
     * *
     * @param childList
     */
    fun setOnCatFilterClickListener(groupName: String,
                                    childList: ArrayList<CategoryChildBean>) {
        mbtnTop.setOnClickListener {
            mbtnTop.setTextColor(Color.WHITE)
            mbtnTop.text = groupName
            if (mExpandOff) {//若分类列表的窗口未打开，则弹出窗口
                mAdapter = CatFilterAdapter(mContext, childList)
                mgvCategory.adapter = mAdapter
                initPopupWindow()
            } else {//否则，关闭窗口
                if (mPopupWindow!!.isShowing) {
                    mPopupWindow!!.dismiss()
                }
            }
            setBtnTopArrow()
        }
    }

    fun release() {
        if (mPopupWindow != null) {
            mPopupWindow!!.dismiss()
        }
    }

    companion object {


        fun getDrawableWidth(context: Context, resId: Int): Int {
            val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            return bitmap.width
        }

        fun getDrawableHeight(context: Context, resId: Int): Int {
            val bitmap = BitmapFactory.decodeResource(context.resources, resId)
            return bitmap.height
        }
    }
}
