package com.example.administrator.kotlin.utils

import android.content.Context
import android.widget.Toast
import com.example.kotlin_demo.application.KotLinApplication


object CommonUtils {
    private var toast: Toast? = null

    fun showLongToast(msg: String) {
        showToast(KotLinApplication.instance!!, msg, Toast.LENGTH_LONG).show()
        //        Toast.makeText(KotLinApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }

    fun showShortToast(msg: String) {
        showToast(KotLinApplication.instance!!, msg, Toast.LENGTH_SHORT).show()
        //        Toast.makeText(KotLinApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }

    fun showLongToast(rId: Int) {
        showLongToast(KotLinApplication.instance!!.getString(rId))
    }

    fun showShortToast(rId: Int) {
        showShortToast(KotLinApplication.instance!!.getString(rId))
    }

    fun showToast(context: Context, msg: String, length: Int): Toast {
        if (toast == null) {
            toast = Toast.makeText(context, msg, length)
        } else {
            toast!!.setText(msg)
        }
        return toast!!
    }
}
