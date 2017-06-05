package com.example.kotlin_demo.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.administrator.kotlin.bean.CategoryChildBean
import com.example.administrator.kotlin.bean.CategoryGroupBean
import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.activity.CategoryGoodsDetailActivity
import com.example.kotlin_demo.application.KotLinApplication
import com.example.myapplication.utils.ImageLoader

/**
 * Created by Administrator on 2017/5/31 0031.
 */
class CategoryAdapter(var context: Context, var grouplist:ArrayList<CategoryGroupBean>, var childlist:ArrayList<ArrayList<CategoryChildBean>>) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): Any {
        return grouplist[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }


    override fun getChildrenCount(groupPosition: Int): Int {
        return childlist[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childlist[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return 0
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,convertView: View?, parent: ViewGroup?): View {
        var holder: ChildViewHolder
        var view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.category_child_item, null)
            holder = ChildViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ChildViewHolder
        }

        val childBean = childlist[groupPosition][childPosition]
        ImageLoader.downloadImg(context,holder.mIvChild, childBean.imageUrl!!)
        holder.mChildName.text = childBean.name
        holder.mChildLinear.setOnClickListener {
            context.startActivity(Intent(context, CategoryGoodsDetailActivity::class.java)
                    .putExtra("CategoryGoodsDetailId",childBean.id)
                    .putExtra("CategoryGroupName", grouplist[groupPosition].name)
                    .putExtra("CategoryChildList", childlist[groupPosition]))
            var sp: SharedPreferences = context.getSharedPreferences(KotLinApplication.DATABASE,Activity.MODE_PRIVATE)
            var edit: SharedPreferences.Editor = sp.edit()
            edit.putString("CategoryGroupName", grouplist[groupPosition].name)
            edit.commit()
        }
        return view
    }
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var holder: GroupViewHolder
        var view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.category_group_item, null)
            holder = GroupViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as GroupViewHolder
        }
        val groupBean = grouplist[groupPosition]
        ImageLoader.downloadImg(context,holder.mIvGroup, groupBean.imageUrl!!)
        holder.mCategoryName.text = groupBean.name
        if (isExpanded) {
            holder.mIvExpand.setImageResource(R.mipmap.expand_off)
        } else {
            holder.mIvExpand.setImageResource(R.mipmap.expand_on)
        }
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    override fun getGroupCount(): Int {
        return grouplist.size
    }

    class GroupViewHolder(itemView: View){
        var mIvGroup = itemView.findViewById(R.id.mIv_Category_group) as ImageView
        var mCategoryName = itemView.findViewById(R.id.tv_Category_name) as TextView
        var mIvExpand = itemView.findViewById(R.id.mIv_Category_expand) as ImageView
    }

    class ChildViewHolder(itemView: View){
        var mChildLinear = itemView.findViewById(R.id.child_linearLayout) as LinearLayout
        var mIvChild = itemView.findViewById(R.id.mIv_Category_child) as ImageView
        var mChildName = itemView.findViewById(R.id.mTv_Category_Child_Name) as TextView
    }

    fun addList(groupList: ArrayList<CategoryGroupBean>, childList: ArrayList<ArrayList<CategoryChildBean>>?) {
        this.grouplist.addAll(groupList)
        this.childlist.addAll(childList!!)
        notifyDataSetChanged()
    }
}