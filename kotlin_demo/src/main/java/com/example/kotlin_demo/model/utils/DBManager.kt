package com.example.administrator.kotlin.utils

import android.content.ContentValues
import android.content.Context

import com.example.administrator.kotlin.bean.User


/**
 * Created by Administrator on 2017/5/10 0010.
 */

class DBManager {

    fun iniDB(context: Context) {
        sHelper = DBOpenHelper.getInstance(context)

    }

    fun saveUser(user: User): Boolean {
        val database = sHelper!!.writableDatabase
        if (database.isOpen) {
            val values = ContentValues()
            values.put(DBOpenHelper.USER_COLUMN_NAME, user.muserName)
            values.put(DBOpenHelper.USER_COLUMN_NICK, user.muserNick)
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_PATH, user.mavatarPath)
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_TYPE, user.mavatarType)
            values.put(DBOpenHelper.USER_COLUMN_AVATAR, user.mavatarId)
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_SUFFIX, user.getMavatarSuffix())
            values.put(DBOpenHelper.USER_COLUMN_AVATAR_UPDATE_TIME, user.mavatarLastUpdateTime)
            val insert = database.replace(DBOpenHelper.USER_TABLE_NAME, null, values)
            return insert > 0
        }
        return false
    }

    @Synchronized fun getUser(username: String): User {
        val database = sHelper!!.writableDatabase
        var user = User()
        if (database.isOpen) {
            val sql = "select * from " + DBOpenHelper.USER_TABLE_NAME + " where " + DBOpenHelper.USER_COLUMN_NAME + "=?"
            val cursor = database.rawQuery(sql, arrayOf(username))
            if (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_NAME))
                val nick = cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_NICK))
                val path = cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_PATH))
                val suffix = cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_SUFFIX))
                val time = cursor.getString(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_UPDATE_TIME))
                val type = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.USER_COLUMN_AVATAR_TYPE))
                user = User(username, nick, id, path, suffix, type, time)
            }
        }
        return user
    }

    companion object {
        private var sHelper: DBOpenHelper? = null
        val instance = DBManager()

    }
}
