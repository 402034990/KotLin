package com.example.kotlin_demo.model.net

import com.example.administrator.kotlin.bean.CartBean
import com.example.administrator.kotlin.bean.CollectBean
import com.example.administrator.kotlin.bean.MessageBean
import com.example.administrator.kotlin.utils.OkHttpUtils


/**
 * Created by Administrator on 2017/5/10 0010.
 */

class UserModel : IUserModel {

    override fun login(context: android.content.Context, userName: String, passWord: String, listener: OnCompleteListener<String>) {
        val utils = OkHttpUtils<String>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_LOGIN)
                .addParam(com.example.kotlin_demo.application.I.User.USER_NAME, userName)
                .addParam(com.example.kotlin_demo.application.I.User.PASSWORD, passWord)
                .targetClass(String::class.java)
                .execute(listener)
    }

    override fun register(context: android.content.Context, userName: String, Nick: String, passWord: String, listener: OnCompleteListener<String>) {
        val utils = OkHttpUtils<String>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_REGISTER)
                .addParam(com.example.kotlin_demo.application.I.User.USER_NAME, userName)
                .addParam(com.example.kotlin_demo.application.I.User.NICK, Nick)
                .addParam(com.example.kotlin_demo.application.I.User.PASSWORD, passWord)
                .post()
                .targetClass(String::class.java)
                .execute(listener)
    }

    override fun updateNick(context: android.content.Context, userName: String, Nick: String, listener: OnCompleteListener<String>) {
        val utils = OkHttpUtils<String>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_UPDATE_USER_NICK)
                .addParam(com.example.kotlin_demo.application.I.User.USER_NAME, userName)
                .addParam(com.example.kotlin_demo.application.I.User.NICK, Nick)
                .targetClass(String::class.java)
                .execute(listener)

    }

    override fun updateAvatar(context: android.content.Context, hxIdName: String, mFile: java.io.File, listener: OnCompleteListener<String>) {
        val utils = OkHttpUtils<String>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_UPDATE_AVATAR)
                .addParam(com.example.kotlin_demo.application.I.NAME_OR_HXID, hxIdName)
                .addParam(com.example.kotlin_demo.application.I.AVATAR_TYPE, com.example.kotlin_demo.application.I.AVATAR_TYPE_USER_PATH)
                .addFile2(mFile)
                .post()
                .targetClass(String::class.java)
                .execute(listener)
    }

    override fun findCarts(context: android.content.Context, userName: String, listener: OnCompleteListener<Array<CartBean>>) {
        val utils = OkHttpUtils<Array<CartBean>>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_FIND_CARTS)
                .addParam(com.example.kotlin_demo.application.I.Cart.USER_NAME, userName)
                .targetClass(Array<CartBean>::class.java)
                .execute(listener)
    }

    override fun addCarts(context: android.content.Context, goods_id: Int, userName: String, count: Int, isChecked: Boolean, listener: OnCompleteListener<MessageBean>) {
        val utils = OkHttpUtils<MessageBean>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_ADD_CART)
                .addParam(com.example.kotlin_demo.application.I.Goods.KEY_GOODS_ID, goods_id.toString() + "")
                .addParam(com.example.kotlin_demo.application.I.Cart.USER_NAME, userName)
                .addParam(com.example.kotlin_demo.application.I.Cart.COUNT, count.toString() + "")
                .addParam(com.example.kotlin_demo.application.I.Cart.IS_CHECKED, isChecked.toString())
                .targetClass(MessageBean::class.java)
                .execute(listener)
    }

    override fun deleteCart(context: android.content.Context, id: Int, listener: OnCompleteListener<MessageBean>) {
        val utils = OkHttpUtils<MessageBean>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_DELETE_CART)
                .addParam(com.example.kotlin_demo.application.I.Cart.ID, id.toString())
                .targetClass(MessageBean::class.java)
                .execute(listener)
    }

    override fun updateCart(context: android.content.Context, id: Int, count: Int, isChecked: Boolean, listener: OnCompleteListener<MessageBean>) {
        val utils = OkHttpUtils<MessageBean>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_UPDATE_CART)
                .addParam(com.example.kotlin_demo.application.I.Cart.ID, id.toString())
                .addParam(com.example.kotlin_demo.application.I.Cart.COUNT, count.toString())
                .addParam(com.example.kotlin_demo.application.I.Cart.IS_CHECKED, isChecked.toString())
                .targetClass(MessageBean::class.java)
                .execute(listener)
    }

    override fun findCollects(context: android.content.Context, userName: String, pageId: Int, pageSize: Int, listener: OnCompleteListener<Array<CollectBean>>) {
        val utils = OkHttpUtils<Array<CollectBean>>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_FIND_COLLECTS)
                .addParam(com.example.kotlin_demo.application.I.Collect.USER_NAME, userName)
                .addParam(com.example.kotlin_demo.application.I.PAGE_ID, pageId.toString() + "")
                .addParam(com.example.kotlin_demo.application.I.PAGE_SIZE, pageSize.toString() + "")
                .targetClass(Array<CollectBean>::class.java)
                .execute(listener)
    }

    override fun isCollect(context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>) {
        val utils = OkHttpUtils<MessageBean>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_IS_COLLECT)
                .addParam(com.example.kotlin_demo.application.I.Collect.GOODS_ID, goodsId.toString() + "")
                .addParam(com.example.kotlin_demo.application.I.Collect.USER_NAME, userName)
                .targetClass(MessageBean::class.java)
                .execute(listener)
    }

    override fun addCollect(context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>) {
        collect(com.example.kotlin_demo.application.I.REQUEST_ADD_COLLECT, context, goodsId, userName, listener)
    }

    private fun collect(action: String, context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>) {
        val utils = OkHttpUtils<MessageBean>(context)
        utils.setRequestUrl(action)
                .addParam(com.example.kotlin_demo.application.I.Goods.KEY_GOODS_ID, goodsId.toString() + "")
                .addParam(com.example.kotlin_demo.application.I.Collect.USER_NAME, userName)
                .targetClass(MessageBean::class.java)
                .execute(listener)
    }

    override fun deleteCollect(context: android.content.Context, goodsId: Int, userName: String, listener: OnCompleteListener<MessageBean>) {
        collect(com.example.kotlin_demo.application.I.REQUEST_DELETE_COLLECT, context, goodsId, userName, listener)
    }

    override fun findCollectsCount(context: android.content.Context, userName: String, listener: OnCompleteListener<MessageBean>) {
        val utils = OkHttpUtils<MessageBean>(context)
        utils.setRequestUrl(com.example.kotlin_demo.application.I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(com.example.kotlin_demo.application.I.Collect.USER_NAME, userName)
                .targetClass(MessageBean::class.java)
                .execute(listener)
    }
}
