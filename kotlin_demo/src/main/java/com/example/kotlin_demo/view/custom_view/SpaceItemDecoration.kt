package com.example.kotlin_demo.view.custom_view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 设置Recyclerview的每个Item的周围间距
 * 使用方法
 * mRecyclerView.addItemDecoration(new SpaceItemDecoration(12));
 */
class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val pos = parent.getChildLayoutPosition(view)
        if (parent.getChildPosition(view) != 0)
            outRect.top = space

        // 设置左右间距
        outRect.set(space / 2, 0, space / 2, 0)

        // 从第二行开始 top = mSpace
        //            if (pos >= I.COLUM_NUM) {
        outRect.top = space
        //            } else {
        //                outRect.top = 0;
        //            }
    }
}
