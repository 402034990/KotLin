package com.example.administrator.kotlin.bean

import java.io.Serializable
import java.util.Arrays

class GoodsDetailsBean : Serializable {

    var id: Int = 0
    var goodsId: Int = 0
    var catId: Int = 0
    var goodsName: String? = null
    var goodsEnglishName: String? = null
    var goodsBrief: String? = null
    var shopPrice: String? = null
    var currencyPrice: String? = null
    var promotePrice: String? = null
    var rankPrice: String? = null
    var goodsThumb: String? = null
    var goodsImg: String? = null
    var addTime: Long = 0
    var shareUrl: String? = null
    var isPromote: Boolean = false
    var properties: Array<PropertiesBean>? = null

    override fun toString(): String {
        return "GoodDetailsBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", catId=" + catId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsEnglishName='" + goodsEnglishName + '\'' +
                ", goodsBrief='" + goodsBrief + '\'' +
                ", shopPrice='" + shopPrice + '\'' +
                ", currencyPrice='" + currencyPrice + '\'' +
                ", promotePrice='" + promotePrice + '\'' +
                ", rankPrice='" + rankPrice + '\'' +
                ", goodsThumb='" + goodsThumb + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", addTime=" + addTime +
                ", shareUrl='" + shareUrl + '\'' +
                ", promote=" + isPromote +
                ", properties=" + Arrays.toString(properties) +
                '}'
    }
}
