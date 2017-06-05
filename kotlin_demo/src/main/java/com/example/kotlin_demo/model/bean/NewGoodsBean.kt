package com.example.administrator.kotlin.bean

import java.io.Serializable

class NewGoodsBean : Serializable {

    /**
     * id : 1
     * goodsId : 7672
     * catId : 0
     * goodsName : 趣味煮蛋模具
     * goodsEnglishName : Kotobuki
     * goodsBrief : 将煮好的鸡蛋…
     * shopPrice : ￥110
     * currencyPrice : ￥140
     * promotePrice : ￥0
     * rankPrice : ￥0
     * promote : false
     * goodsThumb : 201509/thumb_img/7672_thumb_G_14423845719.jpg
     * goodsImg : 201509/goods_img/7672_P_1442389445199.jpg
     * colorId : 4
     * colorName : 绿色
     * colorCode : #59d85c
     * colorUrl : 1
     * addTime : 1442389445    
     */

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
    var isPromote: Boolean = false
    var goodsThumb: String? = null
    var goodsImg: String? = null
    var colorId: Int = 0
    var colorName: String? = null
    var colorCode: String? = null
    var colorUrl: String? = null
    var addTime: Long? = null

    override fun toString(): String {
        return "NewGoodBean{" +
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
                ", promote=" + isPromote +
                ", goodsThumb='" + goodsThumb + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorUrl='" + colorUrl + '\'' +
                ", addTime='" + addTime + '\'' +
                '}'
    }
}
