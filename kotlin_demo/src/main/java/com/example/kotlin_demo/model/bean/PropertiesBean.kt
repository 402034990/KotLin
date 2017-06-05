package com.example.administrator.kotlin.bean

import java.io.Serializable
import java.util.Arrays

class PropertiesBean : Serializable {

    var id: Int = 0
    var goodsId: Int = 0
    var colorId: Int = 0
    var colorName: String? = null
    var colorCode: String? = null
    var colorImg: String? = null
    var colorUrl: String? = null
    var albums: Array<AlbumsBean>? = null

    override fun toString(): String {
        return "PropertiesBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorImg='" + colorImg + '\'' +
                ", colorUrl='" + colorUrl + '\'' +
                ", albums=" + Arrays.toString(albums) +
                '}'
    }
}
