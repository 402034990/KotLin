package com.example.administrator.kotlin.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Administrator on 2017/5/10 0010.
 */

class DBOpenHelper private constructor(context: Context) : SQLiteOpenHelper(context, dataBaseName, null, DBOpenHelper.versioncode) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(FULICENTER_USER_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val versioncode = 1

        val USER_TABLE_NAME: String = "t_fulicenter_user"
        val USER_COLUMN_NAME = "m_user_name"
        val USER_COLUMN_NICK = "m_user_nick"
        val USER_COLUMN_AVATAR = "m_user_avatar_id"
        val USER_COLUMN_AVATAR_PATH = "m_user_avatar_path"
        val USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix"
        val USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type"
        val USER_COLUMN_AVATAR_UPDATE_TIME = "m_user_avatar_update_time"

        val FULICENTER_USER_TABLE_CREATE: String =
                "CREATE TABLE $USER_TABLE_NAME " +
                        "( TEXT PRIMARY KEY,$USER_COLUMN_NICK " +
                        "TEXT,$USER_COLUMN_AVATAR " +
                        "INTEGER,$USER_COLUMN_AVATAR_PATH " +
                        "TEXT,$USER_COLUMN_AVATAR_SUFFIX " +
                        "TEXT,$USER_COLUMN_AVATAR_TYPE " +
                        "INTEGER,$USER_COLUMN_AVATAR_UPDATE_TIME " +
                        "TEXT);"

        private var mHelper: DBOpenHelper? = null

        fun getInstance(context: Context): DBOpenHelper {
            if (mHelper == null) {
                mHelper = DBOpenHelper(context)
            }
            return mHelper as DBOpenHelper
        }

        private val dataBaseName: String
            get() = "cn.ucai.fulicenter.db"
    }
}
