package com.example.administrator.kotlin.utils

import java.util.ArrayList

/**
 * Created by Administrator on 2017/5/15 0015.
 */

class AntiShake {
    private val utils = ArrayList<OneClickUtil>()

    @JvmOverloads fun check(o: Any? = null): Boolean {
        val flag: String
        if (o == null) {
            flag = Thread.currentThread().stackTrace[2].methodName
        } else {
            flag = o.toString()
        }
        for (util in utils) {
            if (util.methodName == flag) {
                return util.check()
            }
        }
        val clickUtil = OneClickUtil(flag)
        utils.add(clickUtil)
        return clickUtil.check()
    }
}
