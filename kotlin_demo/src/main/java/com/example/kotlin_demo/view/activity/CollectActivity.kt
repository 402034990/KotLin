package com.example.kotlin_demo.view.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.administrator.kotlin.bean.CollectBean
import com.example.administrator.kotlin.utils.ResultUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.view.adapter.CollectAdapter
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.application.KotLinApplication
import com.example.kotlin_demo.model.net.IUserModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import kotlinx.android.synthetic.main.activity_collect.*

class CollectActivity : AppCompatActivity() {
    var model: IUserModel? = null
    var mArrayList: ArrayList<CollectBean>? = null
    var mCollectList: ArrayList<CollectBean>? = null
    var pageId = I.PAGE_ID_DEFAULT
    var pd: ProgressDialog? = null
    var manager: GridLayoutManager? = null
    var mAdapter: CollectAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
        initView()
        setOnListener()
        initData(pageId,I.ACTION_DOWNLOAD)
    }

    private fun initData(pageId: Int, action: Int) {
        initDialog()
        model = UserModel()
        val user = KotLinApplication.instance!!.user
        model?.findCollects(this, user!!.muserName!!,pageId,I.PAGE_SIZE_DEFAULT,object : OnCompleteListener<Array<CollectBean>> {
            override fun onSuccess(s: Array<CollectBean>) {
                dismiss()
                mAdapter?.isMore = s!=null&&!s.isEmpty()
                if (s != null) {
                    mArrayList = ResultUtils.array2List(s)
                    if (mArrayList?.size!! < I.PAGE_SIZE_DEFAULT) {
                        center()
                    }

                    if (mAdapter?.isMore!!) {
                        mAdapter?.FootText = resources.getString(R.string.load_more)
                    } else {
                        mAdapter?.FootText = resources.getString(R.string.no_more)
                        return
                    }

                    when (action) {
                        I.ACTION_DOWNLOAD ->{
                            mCollectList?.clear()
                            mCollectList?.addAll(mArrayList!!)
                            mAdapter?.initArrayList(mArrayList!!)
                        }

                        I.ACTION_PULL_DOWN ->{
                            mCollectList?.clear()
                            mCollectList?.addAll(mArrayList!!)
                            collect_hint.visibility = View.GONE
                            Collect_swip.isRefreshing = false
                            mAdapter?.initArrayList(mArrayList!!)
                        }

                        I.ACTION_PULL_UP ->{
                            mCollectList?.addAll(mArrayList!!)
                            mAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onError(error: String) {
                dismiss()
            }

        })
    }

    private fun center() {
        manager?.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                if (position == mAdapter?.itemCount!! - 1) {
                    return manager?.spanCount!!
                }
                return 1
            }

        }
    }

    private fun initDialog() {
        pd = ProgressDialog(this)
        pd?.setMessage(getString(R.string.load_more))
        pd?.show()
    }

    private fun dismiss() {
        if (pd != null) {
            pd?.dismiss()
        }
    }
    private fun initView() {
        manager = GridLayoutManager(this,I.COLUM_NUM)
        mCollectList = ArrayList()
        mAdapter = CollectAdapter(this, mCollectList!!)
        collect_recyclerview.adapter = mAdapter
        collect_recyclerview.layoutManager = manager
    }

    private fun setOnListener() {
        mCollectIv_back.setOnClickListener { finish() }
        onSwipeRefresh()
        onScroll()
    }

    private fun onScroll() {
        collect_recyclerview.setOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var lastPosition = manager?.findLastVisibleItemPosition()
                mAdapter?.isDragging = newState==RecyclerView.SCROLL_STATE_DRAGGING
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition!! <= mAdapter?.itemCount!! - 1 && mAdapter?.isMore!!) {
                    pageId++
                    center()
                    initData(pageId,I.ACTION_PULL_UP)
                }
            }
        })
    }

    private fun onSwipeRefresh() {
        Collect_swip.setOnRefreshListener {
            pageId = 1
            collect_hint.visibility = View.VISIBLE
            Collect_swip.isRefreshing = true
            initData(pageId,I.ACTION_PULL_DOWN)
        }
    }
}
