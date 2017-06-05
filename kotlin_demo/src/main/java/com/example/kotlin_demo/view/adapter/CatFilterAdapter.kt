package com.example.kotlin_demo.view.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.administrator.kotlin.bean.CategoryChildBean

import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.activity.CategoryGoodsDetailActivity
import com.example.myapplication.utils.ImageLoader

import java.util.ArrayList


/**
 * Created by Administrator on 2017/5/9 0009.
 */

class CatFilterAdapter(internal var context: Context,
                       internal var Children: ArrayList<CategoryChildBean>?) : BaseAdapter() {

    override fun getCount(): Int {
        return if (Children == null) 0 else Children!!.size
    }

    override fun getItem(position: Int): CategoryChildBean {
        return Children!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, layout: View?, parent: ViewGroup): View {
        var view = layout
        val holder: ViewChildHolder
        if (view == null) {
            view = View.inflate(context, R.layout.item_cat_filter, null)
            holder = ViewChildHolder(view)
            view!!.tag = holder
        } else {
            holder = view.tag as ViewChildHolder
        }
        val child = getItem(position)
        val name = child.name
        holder.tvCategoryChildName.text = name
        ImageLoader.downloadImg(context, holder.ivCategoryChildThumb, child.imageUrl!!)
        holder.layoutCategoryChild.setOnClickListener {
            context.startActivity(Intent(context, CategoryGoodsDetailActivity::class.java)
                    .putExtra("CategoryGoodsDetailId", child.id)
                    .putExtra("CategoryChildList", Children))
            if (CategoryGoodsDetailActivity::class.java.isInstance(context)) {
                val activity = context as CategoryGoodsDetailActivity
                activity.finish()
            }
        }
        return view
    }

    internal inner class ViewChildHolder(layout: View) {
        var ivCategoryChildThumb: ImageView = layout.findViewById(R.id.ivCategoryChildThumb) as ImageView
        var tvCategoryChildName: TextView = layout.findViewById(R.id.tvCategoryChildName) as TextView
        var layoutCategoryChild: RelativeLayout = layout.findViewById(R.id.layout_category_child) as RelativeLayout
    }
}
