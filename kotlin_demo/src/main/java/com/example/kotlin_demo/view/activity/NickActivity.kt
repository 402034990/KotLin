package com.example.kotlin_demo.view.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.administrator.kotlin.bean.Result
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.CommonUtils
import com.example.administrator.kotlin.utils.ResultUtils
import com.example.administrator.kotlin.utils.SharePrefrenceUtils
import com.example.administrator.kotlin.utils.UserDao

import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.KotLinApplication
import com.example.kotlin_demo.model.net.IUserModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import kotlinx.android.synthetic.main.activity_nick.*


class NickActivity : AppCompatActivity() {

    var user: User? =null
    var model: IUserModel? = null
    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick)
        initView()
    }

    private fun initDialog() {
        pd = ProgressDialog(this)
        pd?.setMessage(getString(R.string.update_user_nick))
        pd?.show()
    }

    private fun dismissDialog() {
        if (pd != null && pd?.isShowing!!) {
            pd?.dismiss()
        }
    }

    private fun initView() {
        user = KotLinApplication.instance!!.user
        etnick.setText(user?.muserNick)
        etnick.selectAll()
        model = UserModel()
    }

    fun onNickClick(v: View) {
        when (v.id) {
            R.id.mnick_back -> {
                finish()
            }
            R.id.save_nick -> {
                updateNick()
            }
        }
    }

    private fun updateNick() {
        initDialog()
        if (checkInfo()) {
            model?.updateNick(this, user?.muserName!!,etnick.text.toString().trim(),object : OnCompleteListener<String> {
                override fun onSuccess(s: String) {
                    val result = ResultUtils.getResultFromJson(s, User::class.java)
                    if (result != null) {
                        Update(result)
                    }
                    dismissDialog()
                }

                override fun onError(error: String) {
                    dismissDialog()
                }

            })
        }
        dismissDialog()
    }

    private fun Update(result: Result<*>?) {
        val user1 = result?.retData as User
        CommonUtils.showLongToast(R.string.update_user_nick_success)
        SharePrefrenceUtils.getInstance().userName = user1.muserName!!
        KotLinApplication.instance!!.user = user1
        val dao = UserDao(this@NickActivity)
        dao.saveUser(user1)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }
    private fun checkInfo(): Boolean {
        if (etnick.text.toString().trim().equals(user?.muserNick)) {
            CommonUtils.showLongToast(R.string.update_nick_fail_unmodify)
            return false
        }
        return true
    }
}
