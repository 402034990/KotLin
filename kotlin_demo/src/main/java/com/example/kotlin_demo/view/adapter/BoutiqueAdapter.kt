package com.example.kotlin_demo.view.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.administrator.kotlin.bean.BoutiqueBean
import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.activity.BoutiqueChildActivity
import com.example.myapplication.utils.ImageLoader

/**
 * Created by Administrator on 2017/5/27 0027.
 */
class BoutiqueAdapter(var context: Context, var list: ArrayList<BoutiqueBean>) : RecyclerView.Adapter<BoutiqueAdapter.BoutiqueViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BoutiqueViewHolder, position: Int) {
        val bean = list[position]
        holder.title.text = bean.title
        holder.boutique_name.text = bean.name
        holder.descript.text = bean.description
        ImageLoader.downloadImg(context,holder.image, bean.imageurl!!)

        holder.image.setOnClickListener {
            context.startActivity(Intent(context, BoutiqueChildActivity::class.java)
                    .putExtra("boutiqueChildId",bean))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BoutiqueViewHolder {
        return BoutiqueViewHolder(View.inflate(context, R.layout.boutique_item,null))
    }

    class BoutiqueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById(R.id.miv_boutique) as ImageView
        var title = itemView.findViewById(R.id.boutique_title) as TextView
        var boutique_name = itemView.findViewById(R.id.boutique_name) as TextView
        var descript = itemView.findViewById(R.id.boutique_descrip) as TextView
    }

    fun addList(mArrayList: java.util.ArrayList<BoutiqueBean>?) {
        if (mArrayList != null) {
            list.clear()
            list.addAll(mArrayList)
            notifyDataSetChanged()
        }
    }
}