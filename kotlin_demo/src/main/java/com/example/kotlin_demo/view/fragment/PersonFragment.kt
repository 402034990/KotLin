package com.example.kotlin_demo.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.kotlin_demo.R
import com.example.kotlin_demo.model.net.IUserModel
import android.widget.SimpleAdapter
import com.example.kotlin_demo.application.KotLinApplication
import kotlinx.android.synthetic.main.fragment_person.*
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import android.content.Intent
import android.util.Log
import com.example.administrator.kotlin.bean.MessageBean
import com.example.administrator.kotlin.bean.User
import com.example.kotlin_demo.view.activity.CollectActivity
import com.example.kotlin_demo.view.activity.SettingsActivity
import com.example.myapplication.utils.ImageLoader


/**
 * A simple [Fragment] subclass.
 */
class PersonFragment : Fragment() {

    var user: User? = null
    var collectCount = 0
    var model: IUserModel? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_person, container, false)
    }


    private fun setOnClick() {
        owner_set.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }
        owner_collect_baby.setOnClickListener {
            startActivity(Intent(activity, CollectActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        owner_collect_count.text = collectCount.toString()
        Log.i("main", "PersonFragment.user:" + KotLinApplication.instance!!.user)
        if (KotLinApplication.instance!!.user != null) {
            user = KotLinApplication.instance!!.user
            owner_name.text = user?.muserNick
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)!!, context, owner_photo)
            initCollectCount()
        }
    }

    private fun initCollectCount() {
        model = UserModel()
        Log.i("main", "username:" + user?.muserName)
        model?.findCollectsCount(context, user?.muserName!!, object : OnCompleteListener<MessageBean> {
            override fun onSuccess(result: MessageBean) {
                if (result.success) {
                    owner_collect_count.text = result.msg
                } else {
                    collectCount = 0
                    owner_collect_count.text = collectCount.toString()
                }
            }

            override fun onError(error: String) {
                Log.i("main", "PersonFragment.error:" + error)
                collectCount = 0
                owner_collect_count.text = collectCount.toString()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOrderList()
        setOnClick()
    }

    private fun initOrderList() {
        val imageList = ArrayList<HashMap<String, Any>>()
        val map1 = HashMap<String, Any>()
        map1.put("image", R.drawable.order_list1)
        imageList.add(map1)
        val map2 = HashMap<String, Any>()
        map2.put("image", R.drawable.order_list2)
        imageList.add(map2)
        val map3 = HashMap<String, Any>()
        map3.put("image", R.drawable.order_list3)
        imageList.add(map3)
        val map4 = HashMap<String, Any>()
        map4.put("image", R.drawable.order_list4)
        imageList.add(map4)
        val map5 = HashMap<String, Any>()
        map5.put("image", R.drawable.order_list5)
        imageList.add(map5)
        val simpleAdapter = SimpleAdapter(context, imageList, R.layout.simple_grid_item, arrayOf("image"), intArrayOf(R.id.image_siplme))
        center_user_gridview.adapter = simpleAdapter
    }
}
