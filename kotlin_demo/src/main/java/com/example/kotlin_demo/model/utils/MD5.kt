package com.example.administrator.kotlin.utils

import java.security.MessageDigest
import kotlin.experimental.and

object MD5 {

    fun getMessageDigest(sb: String): String {
        return getMessageDigest(sb.toString().toByteArray())!!
    }

    fun getMessageDigest(buffer: ByteArray): String? {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        try {
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(buffer)
            val md = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (i in 0..j - 1) {
                val byte0 = md[i]
                //byte0 >>> 4   有问题 未解决
                str[k++] = hexDigits[byte0/(2*4) and 0xf]
                str[k++] = hexDigits[(byte0 and 0xf).toInt()]
            }
            return String(str).toUpperCase()
        } catch (e: Exception) {
            return null
        }

    }
}
