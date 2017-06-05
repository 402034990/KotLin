package com.example.administrator.kotlin.bean

import java.io.Serializable

class BoutiqueBean : Serializable {

    /**
     * id : 262
     * title : 不一样的新妆，不一样的美丽
     * description : 快速增长修护预防脱发洗发水让头发健康快速生长更美丽
     * name : 拯救头发，美丽新妆
     * imageurl : cat_image/boutique1.jpg
     */

    var id: Int = 0
    var title: String? = null
    var description: String? = null
    var name: String? = null
    var imageurl: String? = null

    override fun toString(): String {
        return "BoutiqueBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}'
    }
}
