package com.example.kotlin_demo.view.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.example.kotlin_demo.R
import android.app.ProgressDialog
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.administrator.kotlin.bean.Result
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.MD5
import com.example.administrator.kotlin.utils.ResultUtils
import com.example.administrator.kotlin.utils.SharePrefrenceUtils
import com.example.administrator.kotlin.utils.UserDao
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.application.KotLinApplication
import com.example.kotlin_demo.model.net.IUserModel
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import kotlinx.android.synthetic.main.activity_login.*




class LoginActivity : AppCompatActivity() {
    var model: IUserModel? = null

    var userName: String? = null
    var password: String? = null
    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        model = UserModel()
        login_back.setOnClickListener { finish() }
    }

    private fun initDialog() {
        pd = ProgressDialog(this@LoginActivity)
        pd?.setMessage(getString(R.string.logining))
        pd?.show()
    }

    private fun dismissDialog() {
        if (pd != null) {
            pd?.dismiss()
        }
    }

    fun onLogin(view:View){
        Log.i("main","登陆按钮点击")
        if (checkInfo()){
            initDialog()
            setLogin(userName, MD5.getMessageDigest(password!!))
        }
    }

    private fun setLogin(userName: String?, messageDigest: String?) {
        model?.login(this,userName!!,messageDigest!!,object : OnCompleteListener<String> {
            override fun onSuccess(s: String) {
                if (s != null) {
                    val result = ResultUtils.getResultFromJson(s, User::class.java)
                    if (result != null) {
                        if (result.retCode == MSG_LOGIN_UNKNOW_USER) {
                            Toast.makeText(this@LoginActivity, getString(R.string.login_fail_unknow_user), Toast.LENGTH_SHORT).show()
                        }else if (result.retCode == MSG_LOGIN_UNKNOW_USER) {
                            Toast.makeText(this@LoginActivity, getString(R.string.login_fail_error_password), Toast.LENGTH_SHORT).show()
                        } else {
                            login(result)
                        }
                    }
                }
                dismissDialog()
            }

            override fun onError(error: String) {
                dismissDialog()
            }

        })
    }

    private fun login(result: Result<*>?) {
        val user = result?.retData as User
        KotLinApplication.instance!!.user = user
        SharePrefrenceUtils.getInstance().userName = user.muserName!!
        val dao = UserDao(this@LoginActivity)
        dao.saveUser(user)
        setResult(Activity.RESULT_OK)
        finish()
    }
    private fun checkInfo(): Boolean {
        userName = etuserName.text.toString().trim()
        password = etpassword.text.toString().trim()
        if (TextUtils.isEmpty(userName)) {
            etuserName.requestFocus()
            etuserName.error = getString(R.string.user_name_connot_be_empty)
            return false
        }

        if (TextUtils.isEmpty(password)) {
            etpassword.requestFocus()
            etpassword.error = getString(R.string.password_connot_be_empty);
            return false
        }
        return true
    }

    fun onRegister(view:View){
        startActivityForResult(Intent(this,RegisterActivity::class.java),0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val username = data.getStringExtra(I.User.USER_NAME)
            etuserName.setText(username)
        }
    }

}
