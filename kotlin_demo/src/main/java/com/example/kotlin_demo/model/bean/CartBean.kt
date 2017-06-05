package com.example.administrator.kotlin.bean

import java.io.Serializable


class CartBean : Serializable {

    var id = 0
    var userName: String? = null
    var goodsId: Int = 0
    var count: Int = 0
    var isChecked: Boolean = false
    var goods: GoodsDetailsBean? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is CartBean) return false

        return id == o.id

    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "CartBean{" +
                "id=" + id +
                ", userName=" + userName +
                ", goodsId=" + goodsId +
                ", count=" + count +
                ", checked=" + isChecked +
                ", goods='" + goods + '\'' +
                '}'
    }
}
