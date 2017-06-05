package com.example.kotlin_demo.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.KotLinApplication
import kotlinx.android.synthetic.main.activity_settings.*
import android.content.Intent
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.SharePrefrenceUtils
import com.example.myapplication.utils.ImageLoader


class SettingsActivity : AppCompatActivity(),View.OnClickListener {

    var user: User? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setOnClick()
    }

    private fun setOnClick() {
        msetting_back.setOnClickListener(this)
        modify_nick.setOnClickListener(this)
        LinearLayout_Avatar.setOnClickListener(this)
        exist_login.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        user = KotLinApplication.instance!!.user
        if (user != null) {
            msetting_name.text = user?.muserName
            msetting_nick.text = user?.muserNick
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)!!,this, (msetting_avatar as ImageView?)!!)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.msetting_back -> finish()
            R.id.modify_nick -> startActivity(Intent(this, NickActivity::class.java))
            R.id.LinearLayout_Avatar -> startActivity(Intent(this, UpdateAvatarActivity::class.java))
            R.id.exist_login -> logout()
        }
    }

    private fun logout() {
        KotLinApplication.instance!!.user = null
        SharePrefrenceUtils.getInstance().removeUser()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
