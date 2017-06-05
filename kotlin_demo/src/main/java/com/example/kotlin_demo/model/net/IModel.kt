package com.example.kotlin_demo.model.net

import android.content.Context
import com.example.administrator.kotlin.bean.*


/**
 * Created by Administrator on 2017/5/26 0026.
 */

interface IModel {
    fun loadCategoryGroup(context: Context, listener: OnCompleteListener<Array<CategoryGroupBean>>)

    fun loadCategoryChild(context: Context, parentId: Int, listener: OnCompleteListener<Array<CategoryChildBean>>)

    fun loadCategoryGoodsDetail(context: Context, cat_id: Int, pageId: Int, PageSize: Int, listener: OnCompleteListener<Array<NewGoodsBean>>)

    fun findNewGoods(context: Context, cat_id: Int, page_id: Int, page_size: Int, listener: OnCompleteListener<Array<NewGoodsBean>>)

    fun findGoodDetail(context: Context, goodsId: Int, listener: OnCompleteListener<GoodsDetailsBean>)

    fun findBoutique(context: Context, listener: OnCompleteListener<Array<BoutiqueBean>>)
}
