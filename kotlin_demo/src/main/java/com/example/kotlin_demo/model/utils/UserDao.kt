package com.example.administrator.kotlin.utils

import android.content.Context

import com.example.administrator.kotlin.bean.User


/**
 * Created by Administrator on 2017/5/10 0010.
 */

class UserDao(context: Context) {
    init {
        DBManager.instance.iniDB(context)
    }

    fun getUser(username: String): User {
        return DBManager.instance.getUser(username)
    }

    fun saveUser(user: User): Boolean {
        return DBManager.instance.saveUser(user)
    }
}

