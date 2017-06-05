package com.example.kotlin_demo.view.fragment


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.kotlin.bean.CategoryChildBean
import com.example.administrator.kotlin.bean.CategoryGroupBean
import com.example.administrator.kotlin.utils.ResultUtils

import com.example.kotlin_demo.R
import com.example.kotlin_demo.view.adapter.CategoryAdapter
import com.example.kotlin_demo.model.net.IModel
import com.example.kotlin_demo.model.net.Model
import com.example.kotlin_demo.model.net.OnCompleteListener
import kotlinx.android.synthetic.main.fragment_category.*


/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : Fragment() {
    var mAdapter: CategoryAdapter? = null
    var groupList: ArrayList<CategoryGroupBean>? = null
    var childList: ArrayList<ArrayList<CategoryChildBean>>? = null
    var model: IModel = Model()
    var pd: ProgressDialog? = null
    var groupsize: Int = 0
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
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

    private fun initData() {
        initDialog()
        model.loadCategoryGroup(context, object : OnCompleteListener<Array<CategoryGroupBean>> {
            override fun onSuccess(result: Array<CategoryGroupBean>) {
                if (result != null) {
                    groupList = ResultUtils.array2List(result)
                    for ((i, group) in result.withIndex()) {
                        val id = group.id
                        childList?.add(i, ArrayList<CategoryChildBean>())
                        loadCategoryChildData(i, id)
                    }
                }
                dismissDialog()
            }

            override fun onError(error: String) {
                dismissDialog()
            }

        })
    }

    private fun loadCategoryChildData(i: Int, id: Int) {
        model.loadCategoryChild(context,id,object : OnCompleteListener<Array<CategoryChildBean>> {
            override fun onSuccess(result: Array<CategoryChildBean>) {
                groupsize++
                if (result != null) {
                    val list = ResultUtils.array2List(result)
                    childList?.set(i,list)
                }
                if (groupList?.size == groupsize) {
                    mAdapter?.addList(groupList!!,childList)
                }
            }

            override fun onError(error: String) {
                dismissDialog()
            }
        })
    }

    private fun initView() {
        groupList = ArrayList()
        childList = ArrayList()
        mAdapter = CategoryAdapter(context, groupList!!, childList!!)
        expandablelistview.setAdapter(mAdapter)
    }
}
