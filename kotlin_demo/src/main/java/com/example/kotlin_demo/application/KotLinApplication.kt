package com.example.kotlin_demo.application

import android.app.Application
import com.example.administrator.kotlin.bean.User


/**
 * Created by Administrator on 2017/5/27 0027.
 */

class KotLinApplication : Application() {

    var user: User? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: KotLinApplication? = null
        /** 存储的文件名  */
        val DATABASE = "Database"
        /** 存储后的文件路径：/data/data/<package name>/shares_prefs + 文件名.xml </package> */
        val PATH = "/data/data/code.sharedpreferences/shared_prefs/Database.xml"
    }
}
