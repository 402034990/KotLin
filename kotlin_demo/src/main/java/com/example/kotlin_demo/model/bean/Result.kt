package com.example.administrator.kotlin.bean

import java.io.Serializable

class Result<T> : Serializable {
    var retCode = -1
    var isRetMsg: Boolean = false
    var retData: T? = null

    constructor() {}
    constructor(retMsg: Boolean, retCode: Int) {
        this.isRetMsg = retMsg
        this.retCode = retCode
    }

    constructor(retCode: Int, retMsg: Boolean, retData: T) : super() {
        this.retCode = retCode
        this.isRetMsg = retMsg
        this.retData = retData
    }

    override fun toString(): String {
        return "Result [retCode=$retCode, retMsg=$isRetMsg, retData=$retData]"
    }
}
