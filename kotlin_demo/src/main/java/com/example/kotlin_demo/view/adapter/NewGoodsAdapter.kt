package com.example.kotlin_demo.view.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.administrator.kotlin.bean.NewGoodsBean
import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.activity.GoodsDetailsActivity
import com.example.kotlin_demo.application.I.*
import com.example.myapplication.utils.ImageLoader

import java.util.*

/**
 * Created by Administrator on 2017/5/25 0025.
 */

class NewGoodsAdapter(var context: Context, var list: ArrayList<NewGoodsBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var FootText: String? = null
    var isMore: Boolean? = true


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        var holder: RecyclerView.ViewHolder? = null
        var layout: View
        when (viewType) {
            TYPE_FOOTER -> {
                layout = View.inflate(context, R.layout.newgoods_footer, null)
                holder = FooterViewHolder(layout)
            }
            TYPE_ITEM -> {
                layout = View.inflate(context, R.layout.newgoods_item, null)
                holder = NewGoodsViewHolder(layout)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == itemCount - 1) {
            var footer: FooterViewHolder = holder as FooterViewHolder
            footer.footer.text = FootText
            return
        }
        var itemHolder: NewGoodsViewHolder = holder as NewGoodsViewHolder
        var bean = list[position]
        ImageLoader.downloadImg(context, itemHolder.image, bean.goodsThumb!!)
        itemHolder.newGoodsName.text = bean.goodsName
        itemHolder.newGoodsPrice.text = bean.currencyPrice
        itemHolder.image.setOnClickListener {
            context.startActivity(Intent(context,GoodsDetailsActivity::class.java)
                    .putExtra("GoodsId",bean.goodsId))
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return TYPE_FOOTER
        } else {
            return TYPE_ITEM
        }
    }

    class NewGoodsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.miv_newgoods) as ImageView
        var newGoodsName: TextView = itemView.findViewById(R.id.newgood_name) as TextView
        var newGoodsPrice: TextView = itemView.findViewById(R.id.newgoods_price) as TextView
    }

    class FooterViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var footer: TextView = convertView.findViewById(R.id.tv_footer) as TextView
    }

    fun sort(sortBy: Int) {
        Collections.sort(list) { o1, o2 ->
            var result = 0
            when (sortBy) {
                SORT_BY_PRICE_ASC -> result = getPrice(o1.currencyPrice!!) - getPrice(o2.currencyPrice!!)
                SORT_BY_PRICE_DESC -> result = getPrice(o2.currencyPrice!!) - getPrice(o1.currencyPrice!!)
                SORT_BY_ADDTIME_ASC -> result = (o1.addTime!! - o2.addTime!!).toInt()
                SORT_BY_ADDTIME_DESC -> result = (o2.addTime!! - o1.addTime!!).toInt()
            }
            result
        }
        notifyDataSetChanged()
    }

    private fun getPrice(currentPrice: String): Int {
        val price = currentPrice.substring(currentPrice.indexOf("￥") + 1)
        return Integer.parseInt(price)
    }
    fun initArrayList(mArrayList: ArrayList<NewGoodsBean>?) {
        list.clear()
        list = mArrayList!!
        notifyDataSetChanged()
    }

    fun addAllArrayList(mArrayList: ArrayList<NewGoodsBean>?) {
        list.addAll(mArrayList!!)
        notifyDataSetChanged()
    }
}
