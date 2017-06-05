package com.example.administrator.kotlin.utils

import android.content.Context
import android.os.Handler
import android.os.Message

import com.example.administrator.kotlin.bean.Result
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.KotLinApplication
import com.google.gson.Gson

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLConnection
import java.net.URLEncoder
import java.util.ArrayList
import java.util.Arrays
import java.util.concurrent.TimeUnit

import okhttp3.Cache
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response


/**
 * Created by yao on 2016/9/16.

 */
/**
 * 构造器，mOkHttpClient必须单例，无论创建多少个OkHttpUtils的实例。
 * 都由mOkHttpClient一个对象处理所有的网络请求。
 */
class OkHttpUtils<T>(context: Context) {
    private var mHandler: Handler? = null

    /**
     * 存放post请求的实体，实体中存放File类型的文件
     */
    internal var mFileBody: RequestBody? = null
    internal var mFormBodyBuilder: FormBody.Builder? = null
    internal var mMultipartBodyBuilder: MultipartBody.Builder? = null

    interface OnCompleteListener<T> {
        fun onSuccess(result: T)

        fun onError(error: String)
    }

    private var mListener: OnCompleteListener<T>? = null

    internal var mBuilder: OkHttpClient.Builder? = null

    init {
        //线程安全的单例
        if (mOkHttpClient == null) synchronized(this@OkHttpUtils::class.java) {
                if (mOkHttpClient == null) {
                    mBuilder = OkHttpClient.Builder()
                    //获取sd卡的缓存文件夹
                    val cacheDir = context.externalCacheDir
                    mOkHttpClient = mBuilder!!
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .cache(Cache(cacheDir, (10 * (1 shl 20)).toLong()))//设置缓存位置和缓存大小
                            .build()
                }
            }
        initHandler()
    }

    /**
     * 设置与服务端连接的时限
     * @param connectTime:连接的时限
     * *
     * @return
     */
    fun connectTimeout(connectTime: Int): OkHttpUtils<T> {
        if (mBuilder == null) {
            return this
        }
        mBuilder!!.connectTimeout(connectTime.toLong(), TimeUnit.SECONDS)
        return this
    }

    /**
     * 设置写数据的时限
     * @param writeTimeout：写数据的时限
     * *
     * @return
     */
    fun writeTimeout(writeTimeout: Int): OkHttpUtils<T> {
        if (mBuilder == null) {
            return this
        }
        mBuilder!!.writeTimeout(writeTimeout.toLong(), TimeUnit.SECONDS)
        return this
    }

    /**
     * 设置读取数据的时限
     * @param readTimeout：读取数据的时限
     * *
     * @return
     */
    fun readTimeout(readTimeout: Int): OkHttpUtils<T> {
        if (mBuilder == null) {
            return this
        }
        mBuilder!!.readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
        return this
    }

    /**
     * 设置缓存
     * 第一次请求会请求网络得到数据，第二次以及后面的请求则会从缓存中取出数据
     * @param file:缓存的路径
     * *
     * @param fileSize：缓存的容量
     * *
     * @return
     */
    fun cache(file: File, fileSize: Int): OkHttpUtils<T> {
        if (mBuilder == null) {
            return this
        }
        mBuilder!!.cache(Cache(file, fileSize.toLong()))
        return this
    }


    private fun initHandler() {
        mHandler = object : Handler(KotLinApplication.instance!!.mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    RESULT_ERROR -> mListener!!.onError(if (msg.obj == null) msg.toString() else msg.obj.toString())
                    RESULT_SUCCESS -> {
                        val result = msg.obj as T
                        mListener!!.onSuccess(result)
                    }
                }
            }
        }
    }

    /**
     * 用post请求，添加一个文件
     * @param file:添加至form的文件
     * *
     * @return
     */
    fun addFile(file: File): OkHttpUtils<T> {
        mFileBody = RequestBody.create(null, file)
        return this
    }

    /**
     * 支持设置媒体文件类型的addFile
     * @param type：媒体类型
     * *
     * @param file：添加至form的文件
     * *
     * @return
     */
    fun addFile(type: String, file: File): OkHttpUtils<T> {
        mFileBody = RequestBody.create(MediaType.parse(type), file)
        return this
    }

    fun addFile2(file: File): OkHttpUtils<T> {
        if (mUrl == null) {
            return this
        }
        val fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.name)), file)
        mFileBody = MultipartBody.Builder().addFormDataPart("filename", file.name, fileBody).build()
        return this
    }

    private fun guessMimeType(path: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        var contentTypeFor: String? = fileNameMap.getContentTypeFor(path)
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream"
        }
        return contentTypeFor
    }

    /**
     * 设置为post的请求
     * @return
     */
    fun post(): OkHttpUtils<T> {
        mFormBodyBuilder = FormBody.Builder()
        return this
    }

    internal var mUrl: StringBuilder? = null

    fun url(url: String): OkHttpUtils<T> {
        mUrl = StringBuilder(url)
        return this
    }

    fun setRequestUrl(request: String): OkHttpUtils<T> {
        //http://120.26.242.249:8080/SuperWeChatServerV2.0/register?m_user_name=aaaaaa&m_user_nick=aaaaaa&m_user_password=aaaaaa
        mUrl = StringBuilder(I.SERVER_ROOT)
        mUrl!!.append(request)
        //        Log.e("okhttp","1 murl="+ mUrl.toString());
        return this
    }

    /**
     * 用于json解析的类对象
     */
    var mClazz: Class<T>? = null

    /**
     * 设置json解析的目标类对象
     * @param clazz:解析的类对象
     * *
     * @return
     */
    fun targetClass(clazz: Class<T>): OkHttpUtils<T> {
        mClazz = clazz
        return this
    }

    /**
     * 添加请求参数至url，包括GET和POST请求
     * 不包括POST请求中上传文件的同时向Form中添加其它参数的情况
     * @param key:键
     * *
     * @param value：值
     */
    fun addParam(key: String, value: String): OkHttpUtils<T> {
        try {
            //post请求的request参数也要拼接到url中
            if (mFormBodyBuilder != null) {//post请求的参数添加方式
                mFormBodyBuilder!!.add(key, URLEncoder.encode(value, UTF_8))
            } else {//get请求的参数添加方式
                if (mUrl!!.indexOf("?") == -1) {
                    mUrl!!.append("?")
                } else {
                    mUrl!!.append("&")
                }
                mUrl!!.append(key).append("=").append(URLEncoder.encode(value, UTF_8))
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return this
    }

    /**
     * * post请求,上传文件的同时允许在Form中添加多个参数
     * @param key:参数的键
     * *
     * @param value：参数的值
     * *
     * @return
     */
    fun addFormParam(key: String, value: String): OkHttpUtils<T> {
        if (mMultipartBodyBuilder == null) {
            mMultipartBodyBuilder = MultipartBody.Builder()
            mMultipartBodyBuilder!!.setType(MultipartBody.FORM)
            try {
                mUrl!!.append("?")
                        .append(key)
                        .append("=")
                        .append(URLEncoder.encode(value, UTF_8))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

        } else if (mUrl!!.indexOf("?") > -1) {
            mMultipartBodyBuilder!!.addFormDataPart(key, value)
        }
        return this
    }

    /**
     * post请求中在Form中添加包含上传文件的多个参数
     * @param name:文件的大类型
     * *
     * @param fileName：文件名包括扩展名
     * *
     * @param mediaType：文件的媒体类型
     * *
     * @param file：文件
     * *
     * @return
     */
    fun addFormParam(name: String, fileName: String, mediaType: String, file: File): OkHttpUtils<T> {
        if (mMultipartBodyBuilder == null) {
            return this
        }
        mMultipartBodyBuilder!!.addFormDataPart(name, fileName, RequestBody.create(MediaType.parse(mediaType), file))
        return this
    }

    /**
     * 发送请求
     * @param listener：处理服务端返回结果的代码
     */
    fun execute(listener: OnCompleteListener<T>?) {
        if (listener != null) {
            mListener = listener
        }
        val builder = Request.Builder().url(mUrl!!.toString())
        L.e("url=" + mUrl!!)
        if (mFormBodyBuilder != null) {
            val body = mFormBodyBuilder!!.build()
            builder.post(body)
        }
        if (mFileBody != null) {
            builder.post(mFileBody)
        }
        //如果是post请求向Form中添加多个参数
        if (mMultipartBodyBuilder != null) {
            val multipartBody = mMultipartBodyBuilder!!.build()
            builder.post(multipartBody)
        }
        //创建请求
        val request = builder.build()
        val call = mOkHttpClient!!.newCall(request)
        if (mCallback != null) {
            call.enqueue(mCallback)
            return
        }
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = Message.obtain()
                msg.what = RESULT_ERROR
                msg.obj = e.message
                mHandler!!.sendMessage(msg)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val json = response.body().string()
                if (mClazz == String::class.java) {
                    val msg = Message.obtain()
                    msg.what = RESULT_SUCCESS
                    msg.obj = json
                    mHandler!!.sendMessage(msg)
                } else {
                    val gson = Gson()
                    val value = gson.fromJson(json, mClazz)
                    val msg = Message.obtain()
                    msg.what = RESULT_SUCCESS
                    msg.obj = value
                    mHandler!!.sendMessage(msg)
                }
            }
        })
    }

    internal var mCallback: Callback? = null
    /**
     * 在OkHttp创建的工作线程中执行一段代码,
     * @param callback
     * *
     * @return
     */
    fun doInBackground(callback: Callback): OkHttpUtils<T> {
        mCallback = callback
        return this
    }

    /**
     * 在主线程中执行的代码，doInBackground方法之后调用
     * @param listener
     * *
     * @return
     */
    fun onPostExecute(listener: OnCompleteListener<T>): OkHttpUtils<T> {
        mListener = listener
        return this
    }

    /**doInBackground()之前在主线程中执行的方法，类似与AsyncTask中的onPreExecute()
     * @param r:运行的代码
     * *
     * @return
     */
    fun onPreExecute(r: Runnable): OkHttpUtils<T> {
        r.run()
        return this
    }

    /**
     * 工作线程向主线程发送消息
     * @param msg
     */
    fun sendMessage(msg: Message) {
        mHandler!!.sendMessage(msg)
    }

    /**
     * 重载的sendMessage方法，用于发送空消息
     * @param what
     */
    fun sendMessage(what: Int) {
        mHandler!!.sendEmptyMessage(what)
    }

    fun <T> parseJson(json: String, clazz: Class<*>): T {
        val gson = Gson()
        return gson.fromJson<T>(json, clazz)
    }

    /**
     * 专门针对Result类的json解析方法，不具有通用性，属性定制、专用的方法
     * @param result
     * *
     * @param clazz
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> parseJson(result: Result<*>, clazz: Class<*>): T? {
        if (result.retCode == 0) {
            val json = result.retData!!.toString()
            val t = parseJson<T>(json, clazz)
            return t
        }
        return null
    }

    /**
     * 下载文件，支持更新下载进度
     * @param response：服务端返回的响应类对象
     * *
     * @param file：保存下载文件的File
     * *
     * @throws Exception：IO异常
     */
    @Throws(Exception::class)
    fun downloadFile(response: Response, file: File) {

        val `in` = response.body().byteStream()
        val buffer = ByteArray(1024 * 5)
        //获取文件的字节数
        val fileSize = response.body().contentLength()
        var total = 0//累加下载的字节数
        var percent = 1//下载的预期百分比
        var currentPer: Int//当前下载的百分比
        mHandler!!.sendEmptyMessage(DOWNLOAD_START)
        val out = FileOutputStream(file)
        var len = 0
        do {
            len = `in`.read(buffer)
            out.write(buffer,0,len)
            total+=len
            currentPer = (total*100L/fileSize).toInt()
            if (currentPer >= percent) {
                val msg = Message.obtain()
                msg.what = OkHttpUtils.DOWNLOADING
                msg.arg1 = percent
                sendMessage(msg)
                percent = currentPer+1
            }
        }while (len!=-1)

        sendMessage(OkHttpUtils.DOWNLOAD_FINISH)
    }

    fun <T> array2List(array: Array<T>): ArrayList<T> {
        val list = Arrays.asList(*array)
        val arrayList = ArrayList(list)
        return arrayList
    }

    companion object {
        private val UTF_8 = "utf-8"
        val RESULT_SUCCESS = 0
        val RESULT_ERROR = 1
        val DOWNLOAD_START = 2
        val DOWNLOADING = 3
        val DOWNLOAD_FINISH = 4

        private var mOkHttpClient: OkHttpClient? = null

        /**
         * 释放mClient的资源
         */
        fun release() {
            if (mOkHttpClient != null) {
                //取消所有请求
                mOkHttpClient!!.dispatcher().cancelAll()
                mOkHttpClient = null
            }
        }
    }

}
