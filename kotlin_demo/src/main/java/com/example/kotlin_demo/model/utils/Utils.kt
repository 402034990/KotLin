package com.example.administrator.kotlin.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.kotlin_demo.application.I


import java.util.ArrayList
import java.util.Arrays


/**
 * Created by clawpo on 16/3/28.
 */
object Utils {
    fun getPackageName(context: Context): String {
        return context.packageName
    }

    fun showToast(context: Context, text: String, time: Int) {
        Toast.makeText(context, text, time).show()
    }

    fun showToast(context: Context, strId: Int, time: Int) {
        Toast.makeText(context, strId, time).show()
    }

    /**
     * 将数组转换为ArrayList集合
     * @param ary
     * *
     * @return
     */
    fun <T> array2List(ary: Array<T>): ArrayList<T> {
        val list = Arrays.asList(*ary)
        val arrayList = ArrayList(list)
        return arrayList
    }

    /**
     * 添加新的数组元素：数组扩容
     * @param array：数组
     * *
     * @param t：添加的数组元素
     * *
     * @return：返回添加后的数组
     */
    fun <T> add(array: Array<T>, t: T): Array<T> {
        var array = array
        array = Arrays.copyOf(array, array.size + 1)
        array[array.size - 1] = t
        return array
    }

    /**
     * px转dp
     * @param context
     * *
     * @param msg
     * *
     * @return
     */
    fun getResourceString(context: Context, msg: Int): String? {
        if (msg <= 0) return null
        var msgStr = msg.toString() + ""
        msgStr = I.MSG_PREFIX_MSG + msgStr
        val resId = context.resources.getIdentifier(msgStr, "string", context.packageName)
        return context.resources.getString(resId)
    }

    fun px2dp(context: Context, px: Int): Int {
        val density = context.resources.displayMetrics.density.toInt()
        return px / density
    }

    fun dp2px(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density.toInt()
        return dp * density
    }


}
