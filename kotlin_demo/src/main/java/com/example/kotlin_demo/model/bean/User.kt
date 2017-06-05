package com.example.administrator.kotlin.bean


import com.example.kotlin_demo.application.I

/**
 * Created by clawpo on 2016/10/21.
 */

class User {

    /**
     * muserName : a952700
     * muserNick : 士大夫
     * mavatarId : 72
     * mavatarPath : user_avatar
     * mavatarSuffix : .jpg
     * mavatarType : 0
     * mavatarLastUpdateTime : 1476262984280
     */

    var muserName: String? = null
    var muserNick: String? = null
    var mavatarId: Int = 0
    var mavatarPath: String? = null
    internal var mavatarSuffix: String? = null
    var mavatarType: Int = 0
    var mavatarLastUpdateTime: String? = null

    constructor() {}

    constructor(muserName: String, muserNick: String, mavatarId: Int, mavatarPath: String, mavatarSuffix: String, mavatarType: Int, mavatarLastUpdateTime: String) {
        this.muserName = muserName
        this.muserNick = muserNick
        this.mavatarId = mavatarId
        this.mavatarPath = mavatarPath
        this.mavatarSuffix = mavatarSuffix
        this.mavatarType = mavatarType
        this.mavatarLastUpdateTime = mavatarLastUpdateTime
    }

    fun getMavatarSuffix(): String {
        return if (mavatarSuffix != null) mavatarSuffix!! else I.AVATAR_SUFFIX_JPG
    }

    fun setMavatarSuffix(mavatarSuffix: String) {
        this.mavatarSuffix = mavatarSuffix
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is User) return false

        val user = o

        if (muserName != user.muserName) return false
        return mavatarLastUpdateTime == user.mavatarLastUpdateTime

    }

    override fun hashCode(): Int {
        var result = muserName!!.hashCode()
        result = 31 * result + mavatarLastUpdateTime!!.hashCode()
        return result
    }

    override fun toString(): String {
        return "User{" +
                "muserName='" + muserName + '\'' +
                ", muserNick='" + muserNick + '\'' +
                ", mavatarId=" + mavatarId +
                ", mavatarPath='" + mavatarPath + '\'' +
                ", mavatarSuffix='" + mavatarSuffix + '\'' +
                ", mavatarType=" + mavatarType +
                ", mavatarLastUpdateTime='" + mavatarLastUpdateTime + '\'' +
                '}'
    }
}
