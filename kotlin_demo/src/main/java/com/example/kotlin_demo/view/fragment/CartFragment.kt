package com.example.kotlin_demo.view.fragment


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.administrator.kotlin.bean.CartBean
import com.example.administrator.kotlin.bean.MessageBean
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.ResultUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.adapter.CartAdapter
import com.example.kotlin_demo.model.net.IUserModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import kotlinx.android.synthetic.main.fragment_cart.*
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.application.KotLinApplication


/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {
    var manager: LinearLayoutManager? = null
    var model: IUserModel? = null
    var pd: ProgressDialog? = null
    var user: User? = null
    var CurrentPrice: Int = 0
    var diff: Int = 0
    var mAdapter: CartAdapter? = null
    var mArrayList: ArrayList<CartBean>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        initData(ACTION_DOWNLOAD)
    }

    private fun initData(action: Int) {
        user = KotLinApplication.instance!!.user
        if (user != null) {
            initDialog()
            model?.findCarts(context,user?.muserName!!,object : OnCompleteListener<Array<CartBean>> {
                override fun onSuccess(result: Array<CartBean>) {
                    if (!result.isEmpty()) {
                        ll_cart.visibility = View.VISIBLE
                        mTvNothing.visibility = View.GONE
                        mArrayList = ResultUtils.array2List(result)
                        when (action) {
                            ACTION_DOWNLOAD -> mAdapter?.initArrayList(mArrayList!!)
                            ACTION_PULL_DOWN -> {
                                tvRefreshHint_cart.visibility = View.GONE
                                srl_cart.isRefreshing = false
                                mAdapter?.initArrayList(mArrayList!!)
                            }
                        }
                        initBalance()
                    } else {
                        ll_cart.visibility = View.GONE
                        mTvNothing.visibility = View.VISIBLE
                        mTvNothing.text = "购物车空空如也"
                    }
                    dismiss()
                }

                override fun onError(error: String) {
                    ll_cart.visibility = View.GONE
                    mTvNothing.visibility = View.VISIBLE
                    mTvNothing.text = "购物车空空如也"
                    dismiss()
                }

            })
        }

    }

    private fun setListener() {

        srl_cart.setOnRefreshListener {
            tvRefreshHint_cart.visibility = View.VISIBLE
            srl_cart.isRefreshing = true
            initData(ACTION_PULL_DOWN)
        }
    }

    private fun initDialog() {
        pd = ProgressDialog(context)
        pd?.setMessage(getString(R.string.load_more))
        pd?.show()
    }

    private fun dismiss() {
        if (pd != null) {
            pd?.dismiss()
        }
    }

    var cbListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        var position: Int = buttonView?.tag as Int
        update(position,isChecked)
        mArrayList!![position].isChecked = isChecked
        initBalance()
    }

    var listener = View.OnClickListener {
        v ->
        val position = v.tag as Int
        when(v.id){
            R.id.addCart -> {
                updateCart(position, ACTION_ADD_CART)
            }
            R.id.delCart -> {
                updateCart(position,ACTION_DELETE_CART)
            }
        }
    }

    private fun updateCart(position: Int, count: Int) {
        val cartBean = mArrayList!![position]
        if (cartBean.count > 1) {
            addCart(position, count,cartBean)
        }else if (count < 0) {
            removeCart(position, cartBean)
        } else {
            addCart(position,count,cartBean)
        }
    }

    private fun removeCart(position: Int, cartBean: CartBean) {
        model?.deleteCart(context,cartBean.id,object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    mArrayList?.removeAt(position)
                    mAdapter?.initArrayList(mArrayList!!)
                    initBalance()
                    if (mArrayList?.size == 0) {
                        ll_cart.visibility = View.GONE
                        mTvNothing.visibility = View.VISIBLE
                    }
                }
            }

            override fun onError(error: String) {
            }

        })
    }

    private fun addCart(position: Int, count: Int,cartBean: CartBean) {
        model?.updateCart(context,cartBean.id,cartBean.count+count,cartBean.isChecked,object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                Log.i("main","success:"+result)
                if (result.success) {
                    mArrayList!![position].count = cartBean.count+count
                    mAdapter?.notifyDataSetChanged()
                    initBalance()
                }
            }

            override fun onError(error: String) {
            }

        })
    }
    private fun initBalance() {
        CurrentPrice = 0
        diff = 0
        var RankPrice: Int = 0
        if (mArrayList?.size!! > 0) {
            for (cartBean in mArrayList!!) {
                if (cartBean.isChecked) {
                    val currentIndex = isNumeric(cartBean.goods!!.currencyPrice!!)
                    val diffIndex = isNumeric(cartBean.goods!!.shopPrice!!)
                    CurrentPrice += Integer.parseInt(cartBean.goods!!.currencyPrice!!.substring(currentIndex)) * cartBean.count
                    RankPrice += Integer.parseInt(cartBean.goods!!.rankPrice!!.substring(diffIndex)) * cartBean.count
                    diff = CurrentPrice - RankPrice
                }
            }
        } else {
            CurrentPrice = 0
            diff = 0
        }
        val symbol: String = "合计:￥ "+CurrentPrice.toString()
        val econ: String = "节省:￥ "+diff.toString()
        cart_heji.text = symbol
        cart_jiesheng.text = econ

    }

    private fun update(position: Int?, checked: Boolean) {
        val cartBean = mArrayList!![position!!]
        model?.updateCart(context,cartBean.id,cartBean.count,checked,object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    initBalance()
                }
            }

            override fun onError(error: String) {
            }

        })
    }

    private fun initView() {
        model = UserModel()
        mArrayList = ArrayList()
        manager = LinearLayoutManager(context)
        mAdapter = CartAdapter(context,mArrayList!!)
        mAdapter?.OnListener(cbListener)
        mAdapter?.OnListener(listener)
        recyclerview_cart.adapter = mAdapter
        recyclerview_cart.layoutManager = manager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismiss()
    }
    fun isNumeric(str: String): Int {
        return (0..str.length - 1).firstOrNull { Character.isDigit(str[it]) }
                ?: 0
    }
}
