package com.example.kotlin_demo.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.activity.CollectActivity
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.model.net.IUserModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import android.content.Intent
import com.example.administrator.kotlin.bean.CollectBean
import com.example.administrator.kotlin.bean.MessageBean
import com.example.administrator.kotlin.utils.CommonUtils
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.view.activity.GoodsDetailsActivity
import com.example.myapplication.utils.ImageLoader


/**
 * Created by Administrator on 2017/6/1 0001.
 */
class CollectAdapter(var context: Context, var list: ArrayList<CollectBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isMore = true
    var FootText = ""
    var isDragging = true
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position == itemCount - 1) {
            var footer: FooterViewHolder = holder as FooterViewHolder
            if (itemCount < I.PAGE_SIZE_DEFAULT) {
                footer.mtv_footer.visibility = View.GONE
            } else {
                footer.mtv_footer.text = FootText
            }
            return
        }

        var collect: CollectViewHolder = holder as CollectViewHolder
        val bean = list[position]
        collect.collectName.text = bean.goodsName
        ImageLoader.downloadImg(context, collect.mCollectIv, bean.goodsThumb!!, isDragging)
        collect.collectDelete.setOnClickListener { deleteCollect(bean, position) }
        collect.mCollectIv.setOnClickListener {
            (context as CollectActivity).startActivityForResult(Intent(context, GoodsDetailsActivity::class.java)
                    .putExtra("GoodsId", bean.goodsId), I.REQUEST_CODE_COLLECT)
        }
    }

    private fun deleteCollect(bean: CollectBean, position: Int) {
        var mode: IUserModel = UserModel()
        mode.deleteCollect(context, bean.goodsId, bean.userName!!, object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    list.removeAt(position)
                    CommonUtils.showLongToast(R.string.delete_collect_success)
                    notifyDataSetChanged()
                } else {
                    CommonUtils.showLongToast(R.string.delete_collect_fail)
                }
            }

            override fun onError(error: String) {
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        when (viewType) {
            I.TYPE_FOOTER -> holder = FooterViewHolder(View.inflate(context, R.layout.newgoods_footer, null))
            I.TYPE_ITEM -> holder = CollectViewHolder(View.inflate(context, R.layout.collect_item, null))
        }
        return holder!!
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return I.TYPE_FOOTER
        }
        return I.TYPE_ITEM
    }

    class CollectViewHolder(layout: View) : RecyclerView.ViewHolder(layout) {

        var mCollectIv = layout.findViewById(R.id.mCollectIv) as ImageView
        var collectDelete = layout.findViewById(R.id.collect_delete) as ImageView
        var collectName = layout.findViewById(R.id.collect_name) as TextView
    }

    class FooterViewHolder(layout: View) : RecyclerView.ViewHolder(layout) {
        var mtv_footer = layout.findViewById(R.id.tv_footer) as TextView
    }

    fun initArrayList(mArrayList: ArrayList<CollectBean>) {
        list.clear()
        list.addAll(mArrayList)
        notifyDataSetChanged()
    }
}