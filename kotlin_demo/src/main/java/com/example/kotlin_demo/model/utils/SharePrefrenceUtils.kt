package com.example.administrator.kotlin.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlin_demo.application.KotLinApplication


/**
 * Created by clawpo on 2017/3/21.
 */

class SharePrefrenceUtils {
    internal var sharedPreferences: SharedPreferences
    internal var editor: SharedPreferences.Editor

    init {
        sharedPreferences = KotLinApplication.instance!!.getSharedPreferences(SHARE_PREFRENCE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    var userName: String
        get() = sharedPreferences.getString(SAVE_USERINFO_USERNAME, null)
        set(username) {
            editor.putString(SAVE_USERINFO_USERNAME, username).commit()
        }

    fun removeUser() {
        editor.remove(SAVE_USERINFO_USERNAME).commit()
    }

    companion object {
        private val SHARE_PREFRENCE_NAME = "cn.ucai.fulicenter_save_userinfo"
        private val SAVE_USERINFO_USERNAME = "m_user_username"
        internal var instance: SharePrefrenceUtils? = null

        fun getInstance(): SharePrefrenceUtils {
            if (instance == null) {
                instance = SharePrefrenceUtils()
            }
            return instance!!
        }
    }

}
