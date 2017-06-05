package com.example.administrator.kotlin.utils

import android.app.Activity
import android.content.Context
import android.os.Environment

import java.io.File

object FileUtils {

    /**
     * 获取sd卡的保存位置
     * @param path:
     */
    fun getDir(context: Context, path: String): String {
        var path = path
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //		File dir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        path = dir!!.absolutePath + "/" + path
        return path
    }

    /**
     * 修改本地缓存的图片名称
     * @param context
     * *
     * @param oldImgName
     * *
     * @param newImgName
     */
    fun renameImageFileName(context: Context, oldImgName: String, newImgName: String) {
        var dir = getDir(context, oldImgName)
        val oldFile = File(dir)
        dir = getDir(context, newImgName)
        val newFile = File(dir)
        oldFile.renameTo(newFile)
    }

    /**
     * 返回头像的路径
     * @param avatrType：头像的类型，user_avatar：用户头像，group_icon：群组logo
     * *
     * @param fielName：头像的文件名，如a.jpg
     * *
     * @return
     */
    fun getAvatarPath(activity: Activity, avatrType: String, fielName: String): File {
        var dir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //        File dir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        dir = File(dir, avatrType)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, fielName)
        return file
    }
}
