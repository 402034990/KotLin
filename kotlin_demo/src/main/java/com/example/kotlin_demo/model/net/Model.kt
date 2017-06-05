package com.example.kotlin_demo.model.net

import com.example.administrator.kotlin.bean.*
import com.example.administrator.kotlin.utils.OkHttpUtils
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.I.*


/**
 * Created by Administrator on 2017/5/26 0026.
 */
class Model : IModel {
    override fun loadCategoryGroup(context: android.content.Context, listener: OnCompleteListener<Array<CategoryGroupBean>>) {
        val utils = OkHttpUtils<Array<CategoryGroupBean>>(context)
        utils.setRequestUrl(REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(Array<CategoryGroupBean>::class.java)
                .execute(listener)
    }

    override fun loadCategoryChild(context: android.content.Context, parentId: Int, listener: OnCompleteListener<Array<CategoryChildBean>>) {
        val utils = OkHttpUtils<Array<CategoryChildBean>>(context)
        utils.setRequestUrl(REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(CategoryChild.PARENT_ID, parentId.toString())
                .targetClass(Array<CategoryChildBean>::class.java)
                .execute(listener)
    }

    override fun loadCategoryGoodsDetail(context: android.content.Context, cat_id: Int, pageId: Int, PageSize: Int, listener: OnCompleteListener<Array<NewGoodsBean>>) {
        val utils = OkHttpUtils<Array<NewGoodsBean>>(context)
        utils.setRequestUrl(REQUEST_FIND_GOODS_DETAILS)
                .addParam(GoodsDetails.KEY_CAT_ID, cat_id.toString())
                .addParam(PAGE_ID, pageId.toString())
                .addParam(PAGE_SIZE, PageSize.toString())
                .targetClass(Array<NewGoodsBean>::class.java)
                .execute(listener)
    }

    override fun findBoutique(context: android.content.Context, listener: OnCompleteListener<Array<BoutiqueBean>>) {
        var utils: OkHttpUtils<Array<BoutiqueBean>> = OkHttpUtils(context)
        utils.setRequestUrl(REQUEST_FIND_BOUTIQUES)
                .targetClass(Array<BoutiqueBean>::class.java)
                .execute(listener)
    }

    override fun findGoodDetail(context: android.content.Context, goodsId: Int, listener: OnCompleteListener<GoodsDetailsBean>) {
        var utils: OkHttpUtils<GoodsDetailsBean> = OkHttpUtils(context)
        utils.setRequestUrl(REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.NewGoods.KEY_GOODS_ID,goodsId.toString())
                .targetClass(GoodsDetailsBean::class.java)
                .execute(listener)
    }


    override fun findNewGoods(context: android.content.Context, cat_id: Int, page_id: Int, page_size: Int, listener: OnCompleteListener<Array<NewGoodsBean>>) {
        var utils: OkHttpUtils<Array<NewGoodsBean>> = OkHttpUtils(context)
        utils.setRequestUrl(REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(GoodsDetails.KEY_CAT_ID,""+cat_id)
                .addParam(PAGE_ID,""+page_id)
                .addParam(PAGE_SIZE,""+page_size)
                .targetClass(Array<NewGoodsBean>::class.java)
                .execute(listener)
    }

}