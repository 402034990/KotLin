package com.example.administrator.kotlin.bean

import java.io.Serializable

class CollectBean(var goodsId: Int) : Serializable {

    /**
     * id : 7672
     * userName : 7672
     * goodsId : 7672
     * goodsName : 趣味煮蛋模具
     * goodsEnglishName : Kotobuki
     * goodsThumb : http:121.197.1.20/images/201507/thumb_img/6372_thumb_G_1437108490316.jpg
     * goodsImg : http:121.197.1.20/images/201507/1437108490034171398.jpg
     * addTime : 1442419200000
     */

    var id: Int = 0
    var userName: String? = null
    var goodsName: String? = null
    var goodsEnglishName: String? = null
    var goodsThumb: String? = null
    var goodsImg: String? = null
    var addTime: Long = 0

    override fun toString(): String {
        return "CollectBean{" +
                "id=" + id +
                ", userName=" + userName +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsEnglishName='" + goodsEnglishName + '\'' +
                ", goodsThumb='" + goodsThumb + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", addTime=" + addTime +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is CollectBean) return false

        return goodsId == o.goodsId

    }

    override fun hashCode(): Int {
        return goodsId
    }
}
