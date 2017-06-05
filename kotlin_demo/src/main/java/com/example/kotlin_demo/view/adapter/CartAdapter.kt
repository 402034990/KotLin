package com.example.kotlin_demo.view.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.example.administrator.kotlin.bean.CartBean
import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.activity.GoodsDetailsActivity
import com.example.kotlin_demo.view.activity.MainActivity
import com.example.myapplication.utils.ImageLoader

/**
 * Created by Administrator on 2017/6/1 0001.
 */
class CartAdapter(var context: Context,var list:ArrayList<CartBean>): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }
    var listener: View.OnClickListener? = null
    var changedListener: CompoundButton.OnCheckedChangeListener? = null
    fun OnListener(changeListener: CompoundButton.OnCheckedChangeListener){
        this.changedListener = changeListener
    }

    fun OnListener(listener: View.OnClickListener){
        this.listener = listener
    }
    override fun onBindViewHolder(holder: CartViewHolder?, position: Int) {
        val cartBean = list[position]
        val goods = cartBean.goods
        holder?.cartCheck?.setOnCheckedChangeListener(null)
        ImageLoader.downloadImg(context, holder?.cartIv!!, goods?.goodsThumb!!)
        holder.cartName.text = goods.goodsName
        holder.priceCart.text = goods.currencyPrice
        holder.countCart.text = cartBean.count.toString()
        holder.cartCheck.isChecked = cartBean.isChecked
        holder.cartIv.setOnClickListener {
            (context as MainActivity).startActivityForResult(Intent(context,GoodsDetailsActivity::class.java)
                    .putExtra("GoodsId",cartBean.goodsId),0)
        }
        holder.addCart.setOnClickListener(listener)
        holder.delCart.setOnClickListener(listener)
        holder.cartCheck.setOnCheckedChangeListener(changedListener)
        holder.addCart.tag = position
        holder.delCart.tag = position
        holder.cartCheck.tag = position
    }

    fun initArrayList(mArrayList: ArrayList<CartBean>){
        list.clear()
        list.addAll(mArrayList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CartViewHolder {
        return CartViewHolder(View.inflate(context, R.layout.item_cart,null))
    }

    class CartViewHolder(layout: View) : RecyclerView.ViewHolder(layout){
        var cartCheck = layout.findViewById(R.id.cartCheck) as CheckBox
        var cartIv = layout.findViewById(R.id.cartIv) as ImageView
        var cartName = layout.findViewById(R.id.cartName) as TextView
        var addCart = layout.findViewById(R.id.addCart) as ImageView
        var countCart = layout.findViewById(R.id.countCart) as TextView
        var delCart = layout.findViewById(R.id.delCart) as ImageView
        var priceCart = layout.findViewById(R.id.priceCart) as TextView
    }
}