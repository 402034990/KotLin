package com.example.kotlin_demo.view.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.example.kotlin_demo.R
import android.app.ProgressDialog
import android.text.TextUtils
import com.example.kotlin_demo.model.net.IUserModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import kotlinx.android.synthetic.main.activity_register.*
import com.example.kotlin_demo.application.I.*
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.MD5
import com.example.administrator.kotlin.utils.ResultUtils
import com.example.kotlin_demo.application.I


class RegisterActivity : AppCompatActivity() {
    var model: IUserModel? = null
    var mUserName: String? = null
    var mPassWord: String? = null
    var mNick: String? = null
    var mEtPassWord: String? = null
    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        model = UserModel()
    }

    private fun initDialog() {
        pd = ProgressDialog(this@RegisterActivity)
        pd?.setMessage(getString(R.string.registering))
        pd?.show()
    }

    private fun dismissDialog() {
        if (pd != null) {
            pd?.dismiss()
        }
    }

    fun Register(v: View) {
        initDialog()
        if (checkInput()) {
            model?.register(this, mUserName!!, mNick!!, MD5.getMessageDigest(mPassWord!!), object : OnCompleteListener<String> {
                override fun onSuccess(s: String) {
                    val result = ResultUtils.getResultFromJson(s, User::class.java)
                    if (result!!.retCode == MSG_REGISTER_USERNAME_EXISTS) {
                        registeruserName.requestFocus()
                        registeruserName.error = getString(R.string.register_fail_exists)
                    } else if (result.retCode == MSG_REGISTER_FAIL) {
                        registeruserName.requestFocus()
                        registeruserName.error = getString(R.string.register_fail)
                    } else {
                        Toast.makeText(this@RegisterActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK, Intent().putExtra(I.User.USER_NAME, mUserName))
                        finish()
                    }
                    dismissDialog()
                }

                override fun onError(error: String) {
                    dismissDialog()
                }

            })
        } else {
            dismissDialog()
        }
    }

    private fun checkInput(): Boolean {
        mUserName = registeruserName.text.toString().trim()
        mPassWord = registpassword.text.toString().trim()
        mEtPassWord = registRepassword.text.toString().trim()
        mNick = registeruserNick.text.toString().trim()
        if (TextUtils.isEmpty(mUserName)) {
            registeruserName.requestFocus()
            registeruserName.error = getString(R.string.user_name_connot_be_empty)
            return false
        }
        val num = "[a-zA-Z]\\w{5,15}".toRegex()
        if (!mUserName?.matches(num)!!) {
            registeruserName.requestFocus()
            registeruserName.error = getString(R.string.illegal_user_name)
            return false
        }
        if (TextUtils.isEmpty(mNick)) {
            registeruserNick.requestFocus()
            registeruserNick.error = getString(R.string.nick_name_connot_be_empty)
            return false
        }

        if (TextUtils.isEmpty(mPassWord)) {
            registRepassword.requestFocus()
            registRepassword.error = getString(R.string.password_connot_be_empty)
            return false
        }

        if (TextUtils.isEmpty(mEtPassWord)) {
            registRepassword.requestFocus()
            registRepassword.error = getString(R.string.confirm_password_connot_be_empty)
            return false
        }

        if (!mPassWord.equals(mEtPassWord)) {
            registRepassword.requestFocus()
            registRepassword.error = getString(R.string.two_input_password)
            return false
        }
        return true
    }
}



