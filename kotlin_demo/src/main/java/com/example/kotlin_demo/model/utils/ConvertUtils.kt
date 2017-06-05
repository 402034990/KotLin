package com.example.administrator.kotlin.utils

import android.content.Context

import java.util.ArrayList
import java.util.Arrays

/**
 * 各种转换方法的工具类
 * Created by yao on 2016/10/2.
 */

object ConvertUtils {
    fun <T> array2List(array: Array<T>): ArrayList<T> {
        val list = Arrays.asList(*array)
        val arrayList = ArrayList(list)
        return arrayList
    }

    fun px2dp(context: Context, px: Int): Int {
        val density = context.resources.displayMetrics.density.toInt()
        return px / density
    }

}
