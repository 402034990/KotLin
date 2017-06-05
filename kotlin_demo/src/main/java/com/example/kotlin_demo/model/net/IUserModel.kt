package com.example.kotlin_demo.model.net

import com.example.administrator.kotlin.bean.CartBean
import com.example.administrator.kotlin.bean.CollectBean
import com.example.administrator.kotlin.bean.MessageBean


/**
 * Created by Administrator on 2017/5/10 0010.
 */

interface IUserModel {
    fun login(context: android.content.Context, userName: String, passWord: String, listener: OnCompleteListener<String>)

    fun register(context: android.content.Context, userName: String, Nick: String, pssWord: String, listener: OnCompleteListener<String>)

    fun updateNick(context: android.content.Context, userName: String, Nick: String, listener: OnCompleteListener<String>)

    fun updateAvatar(context: android.content.Context, hxIdName: String, mFile: java.io.File, listener: OnCompleteListener<String>)

    fun findCarts(context: android.content.Context, userName: String, listener: OnCompleteListener<Array<CartBean>>)

    fun addCarts(context: android.content.Context, goods_id: Int, userName: String, count: Int, isChecked: Boolean, listener: OnCompleteListener<MessageBean>)

    fun deleteCart(context: android.content.Context, id: Int, listener: OnCompleteListener<MessageBean>)

    fun updateCart(context: android.content.Context, id: Int, count: Int, isChecked: Boolean, listener: OnCompleteListener<MessageBean>)

    fun findCollects(context: android.content.Context, userName: String, pageId: Int, pageSize: Int, listener: OnCompleteListener<Array<CollectBean>>)

    fun isCollect(context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>)

    fun addCollect(context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>)

    fun deleteCollect(context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>)

    fun findCollectsCount(context: android.content.Context, userName: String, listener: OnCompleteListener<MessageBean>)

}
