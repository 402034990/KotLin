package com.example.administrator.kotlin.bean

import java.io.Serializable

class ColorBean : Serializable {

    /**
     * catId : 262
     * colorId : 1
     * colorName : 灰色
     * colorCode : #959595
     * colorImg : 121.197.1.20/images/201309/1380064809234134935.jpg
     */

    var catId: Int = 0
    var colorId: Int = 0
    var colorName: String? = null
    var colorCode: String? = null
    var colorImg: String? = null

    override fun toString(): String {
        return "ColorBean{" +
                "catId=" + catId +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorImg='" + colorImg + '\'' +
                '}'
    }
}
