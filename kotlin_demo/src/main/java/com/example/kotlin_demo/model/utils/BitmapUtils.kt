package com.example.administrator.kotlin.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtils {
    /**
     * 按指定尺寸转换图片
     * @param data：图片的二进制数据
     * *
     * @param width：图片的预期宽度
     * *
     * @param height：图片的预期高度
     * *
     * @return Bitmap类型
     */
    fun getBitmap(data: ByteArray, width: Int, height: Int): Bitmap {
        val options = Options()
        options.inJustDecodeBounds = true
        //只获取图片的宽和高
        BitmapFactory.decodeByteArray(data, 0, data.size, options)
        var scaleX = 1
        if (width > 0 && width < options.outWidth) {
            scaleX = options.outWidth / width
        }
        var scaleY = 1
        if (height > 0 && height < options.outHeight) {
            scaleY = options.outHeight / height
        }
        var scale = scaleX
        if (scale < scaleY) {
            scale = scaleY
        }
        options.inJustDecodeBounds = false
        options.inSampleSize = scale
        //使用Bitmap.Config.RGB_565比默认的Bitmap.Config.RGB_8888节省一半的内存。
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
        return bitmap
    }

    /**
     * 从本地文件读取图片
     * @param path：图片文件的本地路径
     * *
     * @return 图片的Bitmap类型
     */
    fun getBitmap(path: String): Bitmap? {
        val file = File(path)
        if (!file.exists()) {
            return null
        }
        if (file.length() == 0L) {
            file.delete()
            return null
        }
        val bitmap = BitmapFactory.decodeFile(path)
        return bitmap
    }

    /**
     * 将图片保存至本地
     * @param bitmap：图片
     * *
     * @param path：保存的路径
     * *
     * @throws IOException
     */
    fun saveBitmap(bitmap: Bitmap, path: String) {
        val file = File(path)
        L.e("file=" + file)
        L.e("file.getParentFile=" + file.parentFile)
        L.e("file.exists=" + file.parentFile.exists())
        if (!file.parentFile.exists()) {//若不存在目录，则创建
            val isSuccess = file.parentFile.mkdirs()
            if (!isSuccess) {//若文件所在目录创建失败，则返回
                return
            }
        }
        try {
            val out = FileOutputStream(file)
            bitmap.compress(CompressFormat.JPEG, 100, out)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }
}
