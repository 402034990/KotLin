package com.example.administrator.kotlin.bean

data class MessageBean(var success: Boolean,var msg: String) {

    /**
     * success : true
     * msg : 添加收藏成功
     */

    override fun toString(): String {
        return "MessageBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}'
    }
}
