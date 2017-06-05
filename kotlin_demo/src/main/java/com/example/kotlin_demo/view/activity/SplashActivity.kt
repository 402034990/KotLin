package com.example.kotlin_demo.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.administrator.kotlin.utils.SharePrefrenceUtils
import com.example.administrator.kotlin.utils.UserDao

import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.KotLinApplication
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    var mTimer = MtCountDownTimer(8000, 1000)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mTimer.start()
        setOnClick()
    }

    private fun setOnClick() {
        skip.setOnClickListener {
            mTimer.cancel()
            mTimer.onFinish()
        }
    }

    override fun onStart() {
        super.onStart()
        Thread(Runnable {
            if (KotLinApplication.instance!!.user == null) {
                val username = SharePrefrenceUtils.getInstance().userName
                if (username != null) {
                    val dao = UserDao(this@SplashActivity)
                    val user = dao.getUser(username)
                    if (user != null) {
                        KotLinApplication.instance!!.user = user
                    }
                }
            }
        }).start()
    }

    inner class MtCountDownTimer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            this@SplashActivity.finish()
        }

        override fun onTick(millisUntilFinished: Long) {
            var str = getString(R.string.skip) + " " + millisUntilFinished / 1000 + "s"
            skip.text = str
        }

    }
}
