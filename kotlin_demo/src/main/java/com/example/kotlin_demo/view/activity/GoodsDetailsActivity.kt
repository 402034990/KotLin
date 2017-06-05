package com.example.kotlin_demo.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.administrator.kotlin.bean.GoodsDetailsBean
import com.example.administrator.kotlin.bean.MessageBean
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.AntiShake
import com.example.administrator.kotlin.utils.CommonUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.application.KotLinApplication
import kotlinx.android.synthetic.main.activity_goods_detail.*
import com.example.kotlin_demo.model.net.*


class GoodsDetailsActivity : AppCompatActivity() {

    var goodsId: Int? = null
    var mGoodsDetailsBean: GoodsDetailsBean? = null
    var pd: ProgressDialog? = null
    var user: User? = null
    var collect: Boolean = false
    var util: AntiShake = AntiShake()
    var model: IUserModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_detail)
        goodsId = intent.getIntExtra("GoodsId", 0)
        initData()
    }

    override fun onResume() {
        super.onResume()
        isCollect()
    }

    private fun isCollect() {
        if (KotLinApplication.instance!!.user != null) {
            user = KotLinApplication.instance!!.user
            isCollects()
        } else {
            detail_collect.setImageResource(R.mipmap.bg_collect_in)
        }
    }

    private fun isCollects() {
        model = UserModel()
        model?.isCollect(this, goodsId!!, user?.muserName!!, object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                Log.i("main", "GoodsDetail.result:" + result)
                if (result.success) {
                    detail_collect.setImageResource(R.mipmap.bg_collect_out)
                    collect = true
                } else {
                    detail_collect.setImageResource(R.mipmap.bg_collect_in)
                    collect = false
                }
            }

            override fun onError(error: String) {
            }

        })
    }

    fun onGoodsClick(v: View?) {
        if (util.check(v?.id)) return
        if (KotLinApplication.instance!!.user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            when (v?.id) {
                R.id.detail_collect -> {
                    if (collect) {
                        deleteCollect()
                    } else {
                        addCollect()
                    }
                }

                R.id.detail_cart -> {
                    addCart()
                }
            }
        }
    }

    private fun addCart() {
        model?.addCarts(this, goodsId!!, user?.muserName!!, 1, false, object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    CommonUtils.showLongToast(R.string.add_cart_success)
                }
            }

            override fun onError(error: String) {
                CommonUtils.showLongToast(R.string.add_cart_fail)
            }

        })
    }

    private fun deleteCollect() {
        model?.deleteCollect(this, goodsId!!, user?.muserName!!, object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    detail_collect.setImageResource(R.mipmap.bg_collect_in)
                    CommonUtils.showLongToast(R.string.delete_collect_success)
                    collect = false
                } else {
                    CommonUtils.showLongToast(R.string.delete_collect_fail)
                }
            }

            override fun onError(error: String) {
                CommonUtils.showLongToast(error)
            }

        })
    }

    private fun addCollect() {
        model?.addCollect(this, goodsId!!, user?.muserName!!, object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    detail_collect.setImageResource(R.mipmap.bg_collect_out)
                    CommonUtils.showLongToast(R.string.add_collect_success)
                    collect = true
                } else {
                    CommonUtils.showLongToast(R.string.add_collect_fail)
                }
            }

            override fun onError(error: String) {
                CommonUtils.showLongToast(error)
            }

        })
    }

    private fun initDialog() {
        pd = ProgressDialog(this)
        pd?.setMessage(getString(R.string.load_more))
        pd?.show()
    }

    private fun dismissDialog() {
        if (pd != null) {
            pd?.dismiss()
        }
    }

    private fun initData() {
        var model: IModel = Model()
        initDialog()
        model.findGoodDetail(this, goodsId!!, object : OnCompleteListener<GoodsDetailsBean> {
            override fun onSuccess(result: GoodsDetailsBean) {
                if (result != null) {
                    mGoodsDetailsBean = result
                    detail_EnglishName.text = result.goodsEnglishName
                    detail_name.text = result.goodsName
                    detail_currentprice.text = result.currencyPrice
                    detail_webview.loadDataWithBaseURL(null, result.goodsBrief!!.trim(), TEXT_HTML, UTF_8, null)
                    detail_back.setOnClickListener {
                        finish()
                    }
                    updateColor(0)
                }
                dismissDialog()
            }

            override fun onError(error: String) {
                dismissDialog()
            }

        })
    }

    override fun onStop() {
        super.onStop()
        dismissDialog()
        if (detail_salv != null) {
            detail_salv?.stopPlayLoop()
        }
    }

    private fun updateColor(i: Int) {
        if (mGoodsDetailsBean != null) {
            val album = mGoodsDetailsBean!!.properties!![i].albums
//            val albumsImgUrl:Array<String>(album!!.size)
            var albumsImgUrl = emptyArray<String>()
            for (j in album!!.indices) {
                albumsImgUrl = arrayOf(album[j].imgUrl!!)
            }
            detail_salv!!.startPlayLoop(detail_flowIndicator, albumsImgUrl, albumsImgUrl.size)
        }
    }
}
