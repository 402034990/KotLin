package com.example.kotlin_demo.view.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.administrator.kotlin.bean.BoutiqueBean
import com.example.administrator.kotlin.bean.NewGoodsBean
import com.example.administrator.kotlin.utils.ResultUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.adapter.NewGoodsAdapter
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.model.net.IModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.Model
import kotlinx.android.synthetic.main.activity_boutique_child.*
import kotlinx.android.synthetic.main.fragment_new_goods.*

class BoutiqueChildActivity : AppCompatActivity() {
    var bean: BoutiqueBean? = null
    var mAdapter: NewGoodsAdapter? = null
    var manager: GridLayoutManager? = null
    var mArrayList: ArrayList<NewGoodsBean>? = null
    var PAGE_ID: Int = PAGE_ID_DEFAULT
    val PAGE_SIZE: Int = PAGE_SIZE_DEFAULT
    var action_download: Int = ACTION_DOWNLOAD
    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boutique_child)
        bean = intent.getSerializableExtra("boutiqueChildId") as BoutiqueBean
        Log.i("main","boutiqueBean:"+bean)
        initView()
        iniData(action_download)
        setListener()
    }

    private fun initView() {
        boutiquechild_name.text = bean!!.name
        manager = GridLayoutManager(this, COLUM_NUM)
        mArrayList = ArrayList()
        mAdapter = NewGoodsAdapter(this, mArrayList!!)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = manager
    }
    private fun setListener() {
        setSwipeRefreshListener()
        setRecyclerListener()
        boutique_child_back.setOnClickListener {
            finish()
        }
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
    private fun setRecyclerListener() {
        mRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var lastPosition: Int? = manager?.findLastVisibleItemPosition()
                if (lastPosition != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter?.isMore!! && lastPosition >= mAdapter?.itemCount!! - 1) {
                        PAGE_ID += 1
                        iniData(ACTION_PULL_UP)
                        manager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                            override fun getSpanSize(position: Int): Int {
                                if (position == mAdapter?.itemCount!! - 1) {
                                    return manager?.spanCount!!
                                }
                                return 1
                            }
                        }

                    }
                }
            }
        })

    }

    private fun setSwipeRefreshListener() {
        swipeRefresh.setOnRefreshListener {
            PAGE_ID = 1
            swipeRefresh.isRefreshing = true
            textHint.visibility = View.VISIBLE
            iniData(ACTION_PULL_DOWN)
        }
    }

    private fun iniData(action: Int?) {
        var model: IModel? = Model()
        initDialog()
        model?.findNewGoods(this, bean!!.id, PAGE_ID, PAGE_SIZE, object : OnCompleteListener<Array<NewGoodsBean>> {
            override fun onError(error: String) {
                dismissDialog()
                Log.i("main","response error:"+error)
            }

            override fun onSuccess(result: Array<NewGoodsBean>) {
                mAdapter?.isMore = !(result == null || result.isEmpty())
                if (result != null) {
                    mArrayList = ResultUtils.array2List(result)
                    if (mAdapter?.isMore!!) {
                        mAdapter?.FootText = getString(R.string.load_more)
                    } else {
                        mAdapter?.FootText = getString(R.string.no_more)
                    }
                    when (action) {
                        ACTION_DOWNLOAD -> {
                            mAdapter?.initArrayList(mArrayList)
                        }

                        ACTION_PULL_DOWN -> {
                            swipeRefresh.isRefreshing = false
                            textHint.visibility = View.GONE
                            mAdapter?.initArrayList(mArrayList)
                        }

                        ACTION_PULL_UP -> {
                            mAdapter?.addAllArrayList(mArrayList)
                        }
                    }
                }
                dismissDialog()
            }


        })
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }
}
