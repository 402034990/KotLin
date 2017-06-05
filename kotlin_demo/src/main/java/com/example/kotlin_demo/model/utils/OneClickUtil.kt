package com.example.administrator.kotlin.utils

import java.util.Calendar

/**
 * Created by Administrator on 2017/5/15 0015.
 */

class OneClickUtil(val methodName: String) {
    private var lastClickTime: Long = 0

    fun check(): Boolean {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            return false
        } else {
            return true
        }
    }

    companion object {
        //设置第二次点击的时间
        val MIN_CLICK_DELAY_TIME = 1000
    }
}
