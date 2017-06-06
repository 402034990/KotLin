package com.example.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.support.v4.util.LruCache
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.BitmapUtils
import com.example.administrator.kotlin.utils.FileUtils
import com.example.administrator.kotlin.utils.L
import com.example.kotlin_demo.R
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.I.*


import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Created by yao on 2016/5/18.
 */
class ImageLoader private constructor(baseUrl: String) {
    /** mHandler不能单例，否则一个mHandler不能准确地处理多个mBean */
    private var mHandler: Handler? = null
    internal var mBean: ImageBean? = null

    /** RecyclerView、listView、GridView等容器 */
    internal var mParentLayout: ViewGroup? = null
    /** *缺省图片 */
    private var mDefaultPicId: Int = 0
    /**ListView、RecyclerView是否在拖拽中，true：拖拽中 */
    internal var mIsDragging: Boolean = false

    interface OnImageLoadListener {
        fun onSuccess(url: String, bitmap: Bitmap)

        fun onError(error: String)
    }

    internal inner class ImageBean {
        internal var url: String? = null
        internal var width: Int = 0
        internal var height: Int = 0
        internal var bitmap: Bitmap? = null
        internal var listener: OnImageLoadListener? = null
        internal var saveFileName: String? = null
        internal var error: String? = null
        internal var imageView: ImageView? = null
    }

    init {
        mBean = ImageBean()
        mBean!!.url = baseUrl
        mIsDragging = true
        initHandler()
        if (mOkHttpClient == null) {
            synchronized(ImageLoader::class.java) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = OkHttpClient()
                }
            }
        }
        if (mCaches == null) {
            initCaches()
        }
    }

    private fun initCaches() {
        val maxMemory = Runtime.getRuntime().maxMemory()
        mCaches = object : LruCache<String, Bitmap>(maxMemory.toInt() / 4) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value!!.rowBytes * value.height
            }
        }

    }

    private fun initHandler() {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                try {
                    val bean = msg.obj as ImageBean
                    when (msg.what) {
                        DOWNLOAD_ERROR -> if (bean.listener != null) {
                            bean.listener!!.onError(bean.error!!)
                        } else {
                            Log.e("main", bean.error)
                        }
                        DOWNLOAD_SUCCESS -> if (bean.listener != null)
                            bean.listener!!.onSuccess(mBean!!.url!!, mBean!!.bitmap!!)
                    }
                } catch (e: Exception) {
                    L.e("imageloader", "e=" + e.message)
                }

            }
        }
    }

    /**
     * 获取服务端根地址并返回ImageLoader对象
     * @param url
     * *
     * @return
     */
    fun url(url: String): ImageLoader {
        build(null)
        //        mBean.url=url;
        mBean!!.url += url
        return this
    }

    /**
     * 设置图片的预期宽度并返回ImageLoader对象
     * @param width
     * *
     * @return
     */
    fun width(width: Int): ImageLoader {
        mBean!!.width = width
        return this
    }

    /**
     * 设置图片的预期高度并返回ImageLoader对象
     * @param height
     * *
     * @return
     */
    fun height(height: Int): ImageLoader {
        mBean!!.height = height
        return this
    }

    /**
     * 设置图片保存至sd卡的文件名
     * @param saveFileName
     * *
     * @return
     */
    fun saveFileName(saveFileName: String): ImageLoader {
        mBean!!.saveFileName = saveFileName
        return this
    }

    fun listener(listener: OnImageLoadListener): ImageLoader {
        mBean!!.listener = listener
        return this
    }

    fun addParam(key: String, value: String): ImageLoader {
        try {
            if (mBean!!.url!!.indexOf("?") == -1) {
                mBean!!.url += "?" + key + "=" + URLEncoder.encode(value, UTF_8)
            } else {
                mBean!!.url += "&" + key + "=" + URLEncoder.encode(value, UTF_8)
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return this
    }

    fun loadImage(context: Context): Bitmap? {
        if (mBean!!.url == null) {
            val msg = Message.obtain()
            msg.what = DOWNLOAD_ERROR
            mBean!!.error = "url没有设置"
            msg.obj = mBean
            mHandler!!.sendMessage(msg)
            return null
        }
        if (mCaches!!.get(mBean!!.url!!) != null) {
            return mCaches!!.get(mBean!!.url!!)
        }
        var dir: String? = null
        if (mBean!!.saveFileName != null) {
            dir = FileUtils.getDir(context, mBean!!.saveFileName!!)
        }
        var bitmap: Bitmap? = null
        if (dir != null) {
             bitmap = BitmapUtils.getBitmap(dir)
        }
        if (bitmap != null) {
            return bitmap
        }
        if (!mIsDragging) {//若列表在滑动中，则不加载图片
            return null
        }
        //用图片的下载地址（不包含每个图片的文件名)设置用于取消请求的tag
        mTag = mBean!!.url
        val request = Request.Builder().url(mBean!!.url!!).tag(mTag).build()
        val call = mOkHttpClient!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val msg = Message.obtain()
                msg.what = DOWNLOAD_ERROR
                mBean!!.error = e.message
                msg.obj = mBean
                mHandler!!.sendMessage(msg)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val bitmap = BitmapUtils.getBitmap(response.body().bytes(), mBean!!.width, mBean!!.height)
                if (bitmap != null) {
                    mBean!!.bitmap = bitmap
                    mCaches!!.put(mBean!!.url!!, mBean!!.bitmap!!)
                    if (mBean!!.saveFileName != null) {
                        BitmapUtils.saveBitmap(mBean!!.bitmap!!, FileUtils.getDir(context, mBean!!.saveFileName!!))
                    }

                    val msg = Message.obtain()
                    msg.what = DOWNLOAD_SUCCESS
                    msg.obj = mBean
                    mHandler!!.sendMessage(msg)
                } else {// 发送下载失败的消息
                    val message = Message.obtain()
                    message.what = DOWNLOAD_ERROR
                    mBean!!.error = "图片下载失败"
                    message.obj = mBean
                    mHandler!!.sendMessage(message)
                }
            }
        })
        return null
    }

    /**
     * 则设置图片下载后主线程默认的图片显示代码->mOnImageLoadListener
     * @param parent:View，例如:RecyclerView、ListView等
     * *
     * @return
     */
    fun listener(parent: ViewGroup?): ImageLoader {
        if (parent != null) {
            mBean!!.listener = object : OnImageLoadListener {
                override fun onError(error: String) {
                    val msg = Message.obtain()
                    msg.obj = error
                    msg.arg1 = DOWNLOAD_ERROR
                    mHandler!!.sendMessage(msg)
                }

                //设置下载完成后处理的代码
                override fun onSuccess(url: String, bitmap: Bitmap) {
                    //从RecyclerView中搜索tag值是url的ImageView
                    val iv = parent.findViewWithTag(url) as ImageView
                    iv?.setImageBitmap(bitmap)
                }



            }
            mParentLayout = parent
        }
        return this
    }

    /**
     * 设置显示图片的ImageView
     * @param imageView
     * *
     * @return
     */
    fun imageView(imageView: ImageView): ImageLoader {
        imageView.tag = mBean!!.url
        mBean!!.imageView = imageView
        this.mParentLayout = imageView.parent as ViewGroup
        if (mBean != null) {
            val params = imageView.layoutParams
            mBean!!.width = params.width
            mBean!!.height = params.height
        }
        return this
    }

    /**
     * 设置缺省显示的图片
     * @param defaultPicId
     * *
     * @return
     */
    fun defaultPicture(defaultPicId: Int): ImageLoader {
        mDefaultPicId = defaultPicId
        return this
    }

    /**
     * 设置在拖拽中是否显示图片，默认：true(一直显示图片)
     * true：不拖拽(显示图片)
     * false：拖拽中(不显示图片)
     * @param isDragging
     * *
     * @return
     */
    fun setDragging(isDragging: Boolean): ImageLoader {
        mIsDragging = isDragging
        return this
    }

    /**
     * 封装了图片下载和显示的缺省代码。
     * 若要编写更为灵活的显示图片的代码，可调用loadImage方法
     * @param context
     */
    fun showImage(context: Context) {
        if (mParentLayout != null) {
            listener(mParentLayout)
        }
        val bitmap = loadImage(context)//从内存或sd卡加载图片
        if (bitmap == null) {
            mBean!!.imageView!!.setImageResource(mDefaultPicId)
        } else {
            mBean!!.imageView!!.setImageBitmap(bitmap)
            mBean!!.imageView!!.tag = null
        }
    }

    companion object {
        private val UTF_8 = "utf-8"
        private val DOWNLOAD_SUCCESS = 0
        private val DOWNLOAD_ERROR = 1

        private var mOkHttpClient: OkHttpClient? = null
        private var mCaches: LruCache<String, Bitmap>? = null
        private var mTag: String? = null

        /**
         * 创建ImageLoader对象
         * @param baseUrl:服务端根地址
         * *
         * @return
         */
        fun build(baseUrl: String?): ImageLoader {
            return ImageLoader(baseUrl!!)
        }

        /**
         * 释放ImageLoader类的静态对象
         */
        fun release() {
            if (mOkHttpClient != null) {
                mOkHttpClient = null
                mCaches = null
            }
        }

        fun downloadImg(context: Context, imageView: ImageView, thumb: String) {
            setImage(I.DOWNLOAD_IMG_URL + thumb, context, imageView, true)
        }

        fun downloadImg(context: Context, imageView: ImageView, thumb: String, isDragging: Boolean) {
            setImage(I.DOWNLOAD_IMG_URL + thumb, context, imageView, isDragging)
        }

        fun setImage(url: String, context: Context, imageView: ImageView, isDragging: Boolean) {
            ImageLoader.build(url)
                    .defaultPicture(R.drawable.nopic)
                    .imageView(imageView)
                    .setDragging(isDragging)
                    .showImage(context)
        }

        //http://101.251.196.90:8000/FuLiCenterServerV2.0/downloadAvatar?
        // name_or_hxid=a952700&avatarType=user_avatar&m_avatar_suffix=.jpg&width=200&height=200
        //http://101.251.196.90:8000/FuLiCenterServerV2.0/downloadAvatar?
        // name_or_hxid=a952700&avatarType=0&m_avatar_suffix=.jpg&width=200&height=200
        fun getAvatarUrl(user: User?): String? {
            if (user != null) {
                val url = I.DOWNLOAD_AVATAR_URL + I.NAME_OR_HXID + "=" + user.muserName+I.AND + I.AVATAR_TYPE + "=" + user.mavatarPath + I.AND + I.AVATAR_SUFFIX+"=" + user.mavatarSuffix + I.AND + "width=200&height=200" + "&" + user.mavatarLastUpdateTime
                L.e("useravatar=" + url)
                return url
            }
            return null
        }

        fun setAvatar(url: String, context: Context, imageView: ImageView) {
            ImageLoader.build(url)
                    .defaultPicture(R.drawable.contactlogo)
                    .imageView(imageView)
                    .showImage(context)
        }
    }
}
