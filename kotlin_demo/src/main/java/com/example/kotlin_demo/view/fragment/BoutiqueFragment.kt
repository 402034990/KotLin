package com.example.kotlin_demo.view.fragment


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.kotlin.bean.BoutiqueBean
import com.example.administrator.kotlin.utils.ResultUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.view.adapter.BoutiqueAdapter
import com.example.kotlin_demo.model.net.IModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.Model
import kotlinx.android.synthetic.main.fragment_new_goods.*

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class BoutiqueFragment : Fragment() {
    var mAdapter: BoutiqueAdapter? = null
    var mArrayList: ArrayList<BoutiqueBean>? = null
    var pd: ProgressDialog? = null
    var action_download: Int = I.ACTION_DOWNLOAD
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_new_goods, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        iniData(action_download)
        setSwipeListener()
    }

    private fun setSwipeListener() {
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            textHint.visibility = View.VISIBLE
            iniData(I.ACTION_PULL_DOWN)
        }
    }


    private fun initDialog() {
        pd = ProgressDialog(context)
        pd?.setMessage(getString(R.string.load_more))
        pd?.show()
    }

    private fun dismissDialog() {
        if (pd != null) {
            pd?.dismiss()
        }
    }


    private fun iniData(action: Int) {
        var model: IModel? = Model()
        initDialog()
        model?.findBoutique(context, object : OnCompleteListener<Array<BoutiqueBean>> {
            override fun onSuccess(result: Array<BoutiqueBean>) {
                mArrayList = ResultUtils.array2List(result)
                when (action) {
                    I.ACTION_PULL_DOWN -> {
                        swipeRefresh.isRefreshing = false
                        textHint.visibility = View.GONE
                        mAdapter?.addList(mArrayList)
                    }
                    I.ACTION_DOWNLOAD -> {
                        mAdapter?.addList(mArrayList)
                    }
                }
                dismissDialog()
            }

            override fun onError(error: String) {
                dismissDialog()
                Log.i("main", "response error:" + error)
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }

    private fun initView() {
        var manager = LinearLayoutManager(context)
        mArrayList = ArrayList()
        mAdapter = BoutiqueAdapter(context, mArrayList!!)
        mRecyclerView.layoutManager = manager
        mRecyclerView.adapter = mAdapter
    }
}
