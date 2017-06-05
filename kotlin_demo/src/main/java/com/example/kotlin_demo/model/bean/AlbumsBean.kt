package com.example.administrator.kotlin.bean

import java.io.Serializable

class AlbumsBean : Serializable {

    var pid: Int = 0
    var imgId: Int = 0
    var imgUrl: String? = null
    var thumbUrl: String? = null

    override fun toString(): String {
        return "AlbumsBean{" +
                "pid=" + pid +
                ", imgId=" + imgId +
                ", imgUrl='" + imgUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}'
    }
}
