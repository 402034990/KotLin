package com.example.kotlin_demo.view.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.example.kotlin_demo.R
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.Fragment
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.application.KotLinApplication
import com.example.kotlin_demo.view.fragment.*


class MainActivity : AppCompatActivity(){

    var newGoodFragment: NewGoodsFragment = NewGoodsFragment()
    var boutiqueFragment: BoutiqueFragment = BoutiqueFragment()
    var categoryFragment: CategoryFragment = CategoryFragment()
    var cartFragment: CartFragment = CartFragment()
    var personFragment: PersonFragment = PersonFragment()
    var bt = arrayOfNulls<RadioButton>(5)
    var fragment = arrayOfNulls<Fragment>(5)
    var currentIndex: Int = 0
    var index: Int  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButton()
        initFragment()
        showFragment()

    }

    private fun initFragment() {
        fragment[0] = newGoodFragment
        fragment[1] = boutiqueFragment
        fragment[2] = categoryFragment
        fragment[3] = cartFragment
        fragment[4] = personFragment
    }

    private fun initButton() {
        bt[0] = newgoods
        bt[1] = boutique
        bt[2] = category
        bt[3] = cart
        bt[4] = contact
    }

    private fun showFragment() {
        val bt = supportFragmentManager.beginTransaction()
        bt.add(R.id.framlayout, newGoodFragment)
                .add(R.id.framlayout,boutiqueFragment)
                .show(newGoodFragment)
                .hide(boutiqueFragment)
                .commit()
    }

    fun onChangedCheck(v: View) {
        when (v.id) {
            R.id.newgoods ->{
                index = 0
            }
            R.id.boutique ->{
                index = 1
            }
            R.id.category ->{
                index = 2
            }
            R.id.cart ->{
                if (KotLinApplication.instance!!.user == null) {
                    startActivityForResult(Intent(this, LoginActivity::class.java), I.REQUEST_CODE_LOGIN_FROM_CART)
                } else {
                    index = 3
                }
            }
            R.id.contact ->{
                if (KotLinApplication.instance!!.user == null) {
                    startActivityForResult(Intent(this, LoginActivity::class.java), I.REQUEST_CODE_LOGIN)
                } else {
                    index = 4
                }
            }
        }
        showFragmentIndex()
    }
    private fun showFragmentIndex() {
        val ft = supportFragmentManager.beginTransaction()
        if (!fragment[index]!!.isAdded) {
            ft.add(R.id.framlayout,fragment[index])
        }
        if (index != currentIndex) {
            ft.show(fragment[index])
                    .hide(fragment[currentIndex])
                    .commit()
        }
        currentIndex = index
        setRadioButton()
    }

    private fun setRadioButton() {
        for (i in 0..bt.size - 1) {
            bt[i]!!.isChecked = i == index
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == I.REQUEST_CODE_LOGIN_FROM_CART) {
                index = 3
            }
            if (requestCode == I.REQUEST_CODE_LOGIN) {
                index = 4
            }

            showFragmentIndex()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((index == 4 || index == 3) && KotLinApplication.instance!!.user == null) {
            index = 0
            showFragmentIndex()
        }
    }
}
