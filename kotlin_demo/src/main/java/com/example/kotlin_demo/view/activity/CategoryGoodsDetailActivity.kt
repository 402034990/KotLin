package com.example.kotlin_demo.view.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.administrator.kotlin.bean.CategoryChildBean
import com.example.administrator.kotlin.bean.NewGoodsBean
import com.example.administrator.kotlin.utils.ResultUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.adapter.NewGoodsAdapter
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.model.net.IModel
import com.example.kotlin_demo.model.net.Model
import com.example.kotlin_demo.model.net.OnCompleteListener
import kotlinx.android.synthetic.main.activity_category_goods_detail.*
import com.example.kotlin_demo.application.KotLinApplication


class CategoryGoodsDetailActivity : AppCompatActivity(){
    var sortBy: Int = 0
    var priceAsc = true
    var addTimeAsc = false
    var model: IModel? = null
    var mArrayList: ArrayList<NewGoodsBean>? = null
    var PageId = PAGE_ID_DEFAULT
    var PageSize = PAGE_SIZE_DEFAULT
    var mAdapter: NewGoodsAdapter? = null
    var manager: GridLayoutManager? = null
    var cateId: Int? = null
    var pd: ProgressDialog? = null
    var arrayList: ArrayList<CategoryChildBean>? = null
    var mGroupName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_goods_detail)
        cateId = intent.getIntExtra("CategoryGoodsDetailId", 0)
        var sp = getSharedPreferences(KotLinApplication.DATABASE, MODE_PRIVATE)
        mGroupName = sp.getString("CategoryGroupName", "")
        arrayList = intent.getSerializableExtra("CategoryChildList") as ArrayList<CategoryChildBean>
        catchildFilterButton.text = mGroupName
        catchildFilterButton.setOnCatFilterClickListener(mGroupName, arrayList!!)
        initView()
        initData(ACTION_DOWNLOAD)
        setListener()
    }

    private fun setListener() {
        setSwipeRefreshListener()
        setRecyclerListener()
        setOnBack()
    }

    private fun setOnBack() {
        mIv_back.setOnClickListener {
            finish()
        }
    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.arrow_down -> {
                Log.i("main","arrow_down")
                priceAsc = !priceAsc
                sortBy = if (priceAsc) SORT_BY_PRICE_ASC else SORT_BY_PRICE_DESC
                arrow_down.setImageResource(getImagePrice())
            }
            R.id.arrow_up -> {
                Log.i("main","arrow_up")
                addTimeAsc = !addTimeAsc
                sortBy = if (addTimeAsc) SORT_BY_ADDTIME_ASC else SORT_BY_ADDTIME_DESC
                arrow_up.setImageResource(getImageTime())
            }
        }
        mAdapter?.sort(sortBy)
    }

    private fun getImagePrice(): Int {
        if (priceAsc) {
            return R.drawable.arrow_order_down
        } else {
            return R.drawable.arrow_order_up
        }
    }

    private fun getImageTime(): Int {
        if (addTimeAsc) {
            return R.drawable.arrow_order_down
        } else {
            return R.drawable.arrow_order_up
        }
    }

    private fun setRecyclerListener() {
        category_detail_recyclerview.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var lastPosition: Int? = manager?.findLastVisibleItemPosition()
                if (lastPosition != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter?.isMore!! && lastPosition >= mAdapter?.itemCount!! - 1) {
                        PageId++
                        initData(ACTION_PULL_UP)
                        manager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
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
        swiperefresh.setOnRefreshListener {
            PageId = 1
            swiperefresh.isRefreshing = true
            texthint.visibility = View.VISIBLE
            initData(ACTION_PULL_DOWN)
        }
    }

    private fun initDialog() {
        pd = ProgressDialog(this)
        pd?.setMessage(getString(R.string.load_more))
        pd?.show()
    }

    private fun dismissDialog() {
        pd?.dismiss()
    }

    private fun initData(action: Int?) {
        initDialog()
        model?.loadCategoryGoodsDetail(this, cateId!!, PageId, PageSize, object : OnCompleteListener<Array<NewGoodsBean>> {
            override fun onSuccess(result: Array<NewGoodsBean>) {
                mAdapter?.isMore = !(result == null || result.isEmpty())
                if (result != null) {
                    if (mAdapter?.isMore!!) {
                        mAdapter?.FootText = getString(R.string.load_more)
                    } else {
                        mAdapter?.FootText = getString(R.string.no_more)
                    }

                    mArrayList = ResultUtils.array2List(result)
                    when (action) {
                        ACTION_DOWNLOAD -> {
                            mAdapter?.initArrayList(mArrayList)
                        }
                        ACTION_PULL_UP -> {
                            mAdapter?.addAllArrayList(mArrayList)
                        }

                        ACTION_PULL_DOWN -> {
                            swiperefresh.isRefreshing = false
                            texthint.visibility = View.GONE
                            mAdapter?.initArrayList(mArrayList)
                        }
                    }
                }
                dismissDialog()
            }

            override fun onError(error: String) {
                dismissDialog()
            }


        })
    }

    private fun initView() {

        mArrayList = ArrayList()
        mAdapter = NewGoodsAdapter(this, mArrayList!!)
        manager = GridLayoutManager(this, COLUM_NUM)
        category_detail_recyclerview.adapter = mAdapter
        category_detail_recyclerview.layoutManager = manager
        model = Model()
    }
}
