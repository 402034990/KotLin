package com.example.administrator.kotlin.utils

import android.util.Log

import com.example.administrator.kotlin.bean.CartBean
import com.example.administrator.kotlin.bean.GoodsDetailsBean
import com.example.administrator.kotlin.bean.Result
import com.example.kotlin_demo.application.I
import com.google.gson.Gson

import org.json.JSONArray
import org.json.JSONObject

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.ArrayList
import java.util.Arrays


object ResultUtils {
    fun <T> getResultFromJson(jsonStr: String?, clazz: Class<T>): Result<*>? {
        val result = Result<T>()
        try {
            if (jsonStr == null || jsonStr.isEmpty() || jsonStr.length < 3) return null
            val jsonObject = JSONObject(jsonStr)
            if (!jsonObject.isNull("retCode")) {
                result.retCode = jsonObject.getInt("retCode")
            } else if (!jsonObject.isNull("msg")) {
                result.retCode = jsonObject.getInt("msg")
            }
            if (!jsonObject.isNull("retMsg")) {
                result.isRetMsg = jsonObject.getBoolean("retMsg")
            } else if (!jsonObject.isNull("result")) {
                result.isRetMsg = jsonObject.getBoolean("result")
            }
            if (!jsonObject.isNull("retData")) {
                val jsonRetData = jsonObject.getJSONObject("retData")
                if (jsonRetData != null) {
                    Log.e("Utils", "jsonRetData=" + jsonRetData)
                    val date: String
                    try {
                        date = URLDecoder.decode(jsonRetData.toString(), I.UTF_8)
                        Log.e("Utils", "jsonRetData=" + date)
                        val t = Gson().fromJson(date, clazz)
                        result.retData = t
                        return result

                    } catch (e1: UnsupportedEncodingException) {
                        e1.printStackTrace()
                        val t = Gson().fromJson(jsonRetData.toString(), clazz)
                        result.retData = t
                        return result
                    }

                }
            } else {
                if (jsonObject != null) {
                    Log.e("Utils", "jsonRetData=" + jsonObject)
                    val date: String
                    try {
                        date = URLDecoder.decode(jsonObject.toString(), I.UTF_8)
                        Log.e("Utils", "jsonRetData=" + date)
                        val t = Gson().fromJson(date, clazz)
                        result.retData = t
                        return result

                    } catch (e1: UnsupportedEncodingException) {
                        e1.printStackTrace()
                        val t = Gson().fromJson(jsonObject.toString(), clazz)
                        result.retData = t
                        return result
                    }

                }
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCartFromJson(jsonStr: String?): ArrayList<CartBean>? {
        var list: ArrayList<CartBean>? = null
        try {
            if (jsonStr == null || jsonStr.isEmpty() || jsonStr.length < 3) return null
            val array = JSONArray(jsonStr)
            if (array != null) {
                list = ArrayList<CartBean>()
                for (i in 0..array.length() - 1) {
                    val jsonObject = array.getJSONObject(i)
                    val cart = CartBean()
                    if (!jsonObject.isNull("id")) {
                        cart.id = jsonObject.getInt("id")
                    }
                    if (!jsonObject.isNull("userName")) {
                        cart.userName = jsonObject.getString("userName")
                    }
                    if (!jsonObject.isNull("goodsId")) {
                        cart.goodsId = jsonObject.getInt("goodsId")
                    }
                    if (!jsonObject.isNull("count")) {
                        cart.count = jsonObject.getInt("count")
                    }
                    if (!jsonObject.isNull("isChecked")) {
                        cart.isChecked = false
                    }
                    if (!jsonObject.isNull("goods")) {
                        try {
                            val jsonRetData = jsonObject.getJSONObject("goods")
                            if (jsonRetData != null) {
                                Log.e("Utils", "jsonRetData=" + jsonRetData)
                                val date: String
                                try {
                                    date = URLDecoder.decode(jsonRetData.toString(), I.UTF_8)
                                    Log.e("Utils", "jsonRetData=" + date)
                                    val g = Gson().fromJson(date, GoodsDetailsBean::class.java)
                                    cart.goods = g

                                } catch (e1: UnsupportedEncodingException) {
                                    e1.printStackTrace()
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                    list.add(cart)
                }
                return list
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    fun <T> getListResultFromJson(jsonStr: String?, clazz: Class<T>): Result<*>? {
        val result = Result<T>()
        Log.e("Utils", "jsonStr=" + jsonStr!!)
        try {
            if (jsonStr == null || jsonStr.isEmpty() || jsonStr.length < 3) return null
            val jsonObject = JSONObject(jsonStr)
            if (!jsonObject.isNull("retCode")) {
                result.retCode = jsonObject.getInt("retCode")
            } else if (!jsonObject.isNull("msg")) {
                result.retCode = jsonObject.getInt("msg")
            }
            if (!jsonObject.isNull("retMsg")) {
                result.isRetMsg = jsonObject.getBoolean("retMsg")
            } else if (!jsonObject.isNull("result")) {
                result.isRetMsg = jsonObject.getBoolean("result")
            }
            if (!jsonObject.isNull("retData")) {
                val array = jsonObject.getJSONArray("retData")
                if (array != null) {
                    val list = ArrayList<T>()
                    for (i in 0..array.length() - 1) {
                        val jsonGroupAvatar = array.getJSONObject(i)
                        val ga = Gson().fromJson(jsonGroupAvatar.toString(), clazz)
                        list.add(ga)
                        result.retData = list[i]
                    }
                    return result
                }
            } else {
                val array = JSONArray(jsonStr)
                if (array != null) {
                    val list = ArrayList<T>()
                    for (i in 0..array.length() - 1) {
                        val jsonGroupAvatar = array.getJSONObject(i)
                        val ga = Gson().fromJson(jsonGroupAvatar.toString(), clazz)
                        list.add(ga)
                        result.retData = list[i]
                    }
                    return result
                }
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun <T> array2List(array: Array<T>): ArrayList<T> {
        val list = Arrays.asList(*array)
        val arrayList = ArrayList(list)
        return arrayList
    }

}
