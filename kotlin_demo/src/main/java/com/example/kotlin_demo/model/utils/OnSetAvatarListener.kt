package com.example.administrator.kotlin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import com.example.kotlin_demo.R


import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


/**

 * 获取头像的框架
 * 1、通过拍照获取头像
 * 2、从相册获取头像
 * 3、对头像进行裁剪，拍照的头像保存至sd卡的当前项目文件夹下
 * 技术点
 * 1、启动系统拍照的Activity，实现拍照并保存照片。
 * 2、启动系统的相册Activity，从相册获取头像
 * 3、启动系统的裁剪Activity，对头像进行裁剪并保存。
 * 4、监听拍照和从相册选取头像两个按钮的单击事件
 * 5、用PopuWindow实现悬浮窗口显示/隐藏
 * 6、处理拍照、相册选取、裁剪三个Activity返回值
 * 如此，调用本框架的Activity只需在启动处和onActivityResult是两个命令即可实现获取头像的
 * 功能。
 * Created by yao on 2016/3/19.

 */
class OnSetAvatarListener
/**
 * 构造器
 * @param mActivity：PopuWindow宿主Activity
 * *
 * @param parentId：宿主Activity的布局的id
 * *
 * @param userName：个人账号和群号
 * *
 * @param avatarType：头像类型：user_avatar或group_icon
 */
(private val mActivity: Activity, parentId: Int,
 /**账号 */
 internal var mUserName: String,
 /**
  * 头像类型：
  * user_avatar:个人头像
  * group_cion:群主logo
  */
 internal var mAvatarType: String) : View.OnClickListener {
    /** popuWindos的布局view */
    private val mLayout: View? = null

    internal var mPopuWindow: PopupWindow? = null

    init {

        //获取父容器的view
        val parentLayout = mActivity.findViewById(parentId)
        /*//获取PopuWindow的布局view
        mLayout= View.inflate(mActivity, R.layout.popu_show_avatar,null);

        //设置拍照和从相册选取两个按钮的单击事件响应
        mLayout.findViewById(R.id.btn_take_picture).setOnClickListener(this);
        mLayout.findViewById(R.id.btn_choose_photo).setOnClickListener(this);*/

        //显示PopuWindow
        showPopupWindow(parentLayout)
    }//成员变量赋值

    /**
     * 显示选择拍照和从相册选取两个按钮的PopuWindow
     * @param parentLayout
     */
    private fun showPopupWindow(parentLayout: View) {
        mPopuWindow = PopupWindow(mLayout, screenDisplay.widthPixels, (90 * screenDisplay.density).toInt())
        //设置触摸PopupWindow之外的区域能关闭PopupWindow
        mPopuWindow!!.isOutsideTouchable = true
        //设置PopupWindow可触摸
        mPopuWindow!!.isTouchable = true
        //设置PopupWindow获取焦点
        mPopuWindow!!.isFocusable = true
        //设置popuWindow的背景,必须设置，否则PopupWindow不能隐藏
        mPopuWindow!!.setBackgroundDrawable(BitmapDrawable())
        //设置popuWindow进入和隐藏的动画
        mPopuWindow!!.animationStyle = R.style.styles_pop_window
        //设置PopuWindow从屏幕底部进入
        mPopuWindow!!.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0)
    }

    /**
     * 获取表示屏幕尺寸、密度等信息的对象
     * @return
     */
    private //创建用于获取屏幕尺寸、像素密度的对象
            //创建用于获取屏幕尺寸、像素密度等信息的对象
    val screenDisplay: DisplayMetrics
        get() {
            val defaultDisplay = mActivity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            defaultDisplay.getMetrics(outMetrics)
            return outMetrics
        }

    override fun onClick(v: View) {
        /*switch (v.getId()) {
            case R.id.btn_take_picture:
                takePicture();
                break;
            case R.id.btn_choose_photo:
                choosePhoto();
                break;
        }*/
    }

    /**
     * 从相册选择头像，启动系统预定义的Activity并要求返回结果
     */
    private fun choosePhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        mActivity.startActivityForResult(intent, REQUEST_CHOOSE_PHOTO)
    }

    /**拍照:启动系统拍照的Activity，要求返回拍照结果 */
    private fun takePicture() {
        val file = FileUtils.getAvatarPath(mActivity, mAvatarType, mUserName + ".jpg")
        val uri = Uri.fromFile(file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        mActivity.startActivityForResult(intent, REQUEST_TAKE_PICTURE)
    }

    /**
     * 关闭PopuWindow
     */
    fun closePopuAvatar() {
        if (mPopuWindow != null) {
            mPopuWindow!!.dismiss()
        }
    }

    /**
     * 设置拍照或从相册选择返回的结果，本方法在Activity.onActivityResult()调用
     * @param requestCode
     * *
     * @param data
     * *
     * @param ivAvatar
     */
    fun setAvatar(requestCode: Int, data: Intent?, ivAvatar: ImageView) {
        when (requestCode) {
            REQUEST_CHOOSE_PHOTO -> if (data != null) {
                startCropPhotoActivity(data.data, 200, 200, REQUEST_CROP_PHOTO)
            }
            REQUEST_TAKE_PICTURE -> if (data != null) {
                startCropPhotoActivity(data.data, 200, 200, REQUEST_CROP_PHOTO)
            }
            REQUEST_CROP_PHOTO -> {
                saveCropAndShowAvatar(ivAvatar, data!!)
                closePopuAvatar()
            }
        }
    }

    /**
     * 保存头像至sd卡的Android文件夹，并显示头像
     * @param ivAvatar
     * *
     * @param data
     */
    private fun saveCropAndShowAvatar(ivAvatar: ImageView, data: Intent) {
        L.e(TAG, "data=" + data)
        val extras = data.extras
        L.e(TAG, "extras=" + extras!!)
        if (extras == null) {
            return
        }
        val avatar = extras.getParcelable<Bitmap>("data") ?: return
        ivAvatar.setImageBitmap(avatar)
        val file = FileUtils.getAvatarPath(mActivity, mAvatarType, mUserName + ".jpg")
        L.e(TAG, "file=" + file.absolutePath)
        if (!file.parentFile.exists()) {
            Toast.makeText(mActivity, "照片保存失败,保存的路径不存在", Toast.LENGTH_LONG).show()
            return
        }
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.i("main", "头像保存失败")
        }

    }

    /**
     * 启动裁剪的Activity
     * @param uri
     * *
     * @param outputX
     * *
     * @param outputY
     * *
     * @param requestCode
     */
    private fun startCropPhotoActivity(uri: Uri, outputX: Int, outputY: Int, requestCode: Int) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("return-data", true)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        mActivity.startActivityForResult(intent, requestCode)
    }

    companion object {
        private val TAG = "OnSetAvatarListener"
        private val REQUEST_TAKE_PICTURE = 1
        private val REQUEST_CHOOSE_PHOTO = 2
        val REQUEST_CROP_PHOTO = 3

        /**
         * 保存头像至sd卡的Android文件夹
         * @param data
         */
        fun saveCropAndShowAvatar(data: Intent, context: Activity, avatarType: String, avatarName: String): File? {
            val extras = data.extras
            val avatar = extras.getParcelable<Bitmap>("data") ?: return null
            val file = FileUtils.getAvatarPath(context, avatarType, avatarName + ".jpg")
            if (!file.parentFile.exists()) {
                Toast.makeText(context, "照片保存失败,保存的路径不存在", Toast.LENGTH_LONG).show()
                return null
            }
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(file)
                avatar.compress(Bitmap.CompressFormat.JPEG, 100, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.i("main", "头像保存失败")
            }

            return file
        }

        /**
         * 返回拍照文件保存的位置
         * @return
         */
        fun getAvatarFile(activity: Activity, avatar: String): File? {
            val dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file: File
            try {
                file = File(dir, avatar)
                var isExists = file.parentFile.exists()
                if (!isExists) {
                    isExists = file.parentFile.mkdirs()
                }
                if (isExists) {
                    return file
                }
            } catch (e: Exception) {
                return null
            }

            return null
        }

        /**
         * 返回头像保存在sd卡的位置:
         * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
         * @param context
         * *
         * @param path
         * *
         * @return
         */
        fun getAvatarPath(context: Context, path: String): String {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val folder = File(dir, path)
            if (!folder.exists()) {
                folder.mkdir()
            }
            return folder.absolutePath
        }
    }
}
