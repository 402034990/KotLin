package com.example.administrator.kotlin.bean

import java.io.Serializable

class CategoryChildBean : Serializable {

    /**
     * id : 348
     * parentId : 344
     * name : 败姐推荐
     * imageUrl : cat_image/256_4.png
     */

    var id: Int = 0
    var parentId: Int = 0
    var name: String? = null
    var imageUrl: String? = null

    override fun toString(): String {
        return "CategoryChildBean{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}'
    }
}
