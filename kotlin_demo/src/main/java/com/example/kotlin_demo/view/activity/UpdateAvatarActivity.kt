package com.example.kotlin_demo.view.activity

import android.Manifest
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.example.kotlin_demo.R
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.ImageView
import com.example.kotlin_demo.model.net.IUserModel
import kotlinx.android.synthetic.main.activity_update_avatar.*
import java.io.File
import android.text.TextUtils
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.administrator.kotlin.CircleImageView
import com.example.administrator.kotlin.bean.Result
import com.example.administrator.kotlin.bean.User
import com.example.administrator.kotlin.utils.CommonUtils
import com.example.administrator.kotlin.utils.ResultUtils
import com.example.administrator.kotlin.utils.SharePrefrenceUtils
import com.example.administrator.kotlin.utils.UserDao
import com.example.kotlin_demo.application.I
import com.example.kotlin_demo.application.I.*
import com.example.kotlin_demo.application.KotLinApplication
import com.example.kotlin_demo.model.net.OnCompleteListener
import com.example.kotlin_demo.model.net.UserModel
import com.example.myapplication.utils.ImageLoader
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException


class UpdateAvatarActivity : AppCompatActivity(), View.OnClickListener {

    //请求相机
    private val REQUEST_CAPTURE = 100
    //请求相册
    private val REQUEST_PICK = 101
    //请求截图
    private val REQUEST_CROP_PHOTO = 102
    //请求访问外部存储
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 103
    //请求写入外部存储
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104
    //头像1
    private val headImage1: CircleImageView? = null
    //头像2
    private val headImage2: ImageView? = null
    //调用照相机返回图片临时文件
    private var tempFile: File? = null
    // 1: qq, 2: weixin
    private var type: Int = 0

    var model: IUserModel? = null
    var user: User? = null
    var pd: ProgressDialog? = null

    var avatarTimeName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_avatar)
        qqLayout.setOnClickListener(this)
        weixinLayout.setOnClickListener(this)
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState)
        model = UserModel()
    }


    private fun initDialog() {
        pd = ProgressDialog(this)
        pd?.setMessage(getString(R.string.update_user_avatar))
        pd?.show()
    }

    private fun dismissDialog() {
        if (pd != null) {
            pd?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        initAvatar()
    }

    private fun initAvatar() {
        user = KotLinApplication.instance!!.user
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)!!, this, (head_image1 as ImageView?)!!)
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user)!!, this, (head_image2 as ImageView?)!!)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera()
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto()
            } else {
                // Permission Denied
            }
        }
    }

    private fun uploadHeadImage() {
        var view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null)
        var btnCamera =view . findViewById (R.id.btn_camera)
        var btnPhoto =view . findViewById (R.id.btn_photo)
        var btnCancel =view . findViewById (R.id.btn_cancel)
        var popupWindow = PopupWindow (view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow.isOutsideTouchable = true;
        var parent = LayoutInflater . from (this).inflate(R.layout.activity_update_avatar, null)
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0)


        btnCamera.setOnClickListener{
                //权限判断
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
                } else {
                    //跳转到调用系统相机
                    gotoCamera()
                }
                popupWindow.dismiss()
            }

        btnPhoto.setOnClickListener{
            //权限判断
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请READ_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_REQUEST_CODE)
            } else {
                //跳转到调用系统图库
                gotoPhoto()
            }
            popupWindow.dismiss()
        }

        btnCancel.setOnClickListener{
            popupWindow.dismiss()
        }
    }

    /**
     * 跳转到相册
     */
    private fun gotoPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK)
    }

    /**
     * 跳转到照相机
     */
    private fun gotoCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile))
        startActivityForResult(intent, REQUEST_CAPTURE)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.qqLayout -> {
                type = 1
                uploadHeadImage()
            }

            R.id.weixinLayout -> {
                type = 2
                uploadHeadImage()
            }
        }
    }

    /**
     * 创建调用系统照相机待存储的临时文件

     * @param savedInstanceState
     */
    private fun createCameraTempFile(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = savedInstanceState.getSerializable("tempFile") as File
        } else {
            tempFile = File(checkDirPath(Environment.getExternalStorageDirectory().path + "/image/"),
                    System.currentTimeMillis().toString() + ".jpg")
        }
    }

    /**
     * 检查文件是否存在
     */
    private fun checkDirPath(dirPath: String): String {
        if (TextUtils.isEmpty(dirPath)) {
            return ""
        }
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dirPath
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("tempFile", tempFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        when (requestCode) {
            REQUEST_CAPTURE //调用系统相机返回
            -> if (resultCode == Activity.RESULT_OK) {
                gotoClipActivity(Uri.fromFile(tempFile))
            }
            REQUEST_PICK  //调用系统相册返回
            -> if (resultCode == Activity.RESULT_OK) {
                val uri = intent.data
                gotoClipActivity(uri)
            }
            REQUEST_CROP_PHOTO  //剪切图片返回
            -> if (resultCode == Activity.RESULT_OK) {
                val uri = intent.data ?: return
                val cropImagePath = getRealFilePathFromUri(applicationContext, uri)
                updateAvatar(cropImagePath)
            }
        }
    }

    private fun updateAvatar(cropImagePath: String) {
        val bitmap = BitmapFactory.decodeFile(cropImagePath)
        val file = saveBitmapFile(bitmap)
        if (file != null) {
            updateUserAvatar(file)
        }
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

    private fun saveBitmapFile(bitmap: Bitmap?): File? {
        if (bitmap != null) {
            val imagePath = getAvatarPath(this@UpdateAvatarActivity, I.AVATAR_TYPE) + "/" + getAvatarName() + ".jpg"
            val file = File(imagePath)//将要保存图片的路径
            try {
                val bos = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        }
        return null
    }


    private fun getAvatarName(): String {
        avatarTimeName = System.currentTimeMillis().toString()
        return avatarTimeName
    }

    private fun updateUserAvatar(file: File) {
        val user = KotLinApplication.instance!!.user
        initDialog()
        model?.updateAvatar(this, user!!.muserName!!, file, object : OnCompleteListener<String> {
            override fun onSuccess(s: String) {
                val result = ResultUtils.getResultFromJson(s, User::class.java)
                if (result != null) {
                    if (result.retCode == I.MSG_UPLOAD_AVATAR_FAIL) {
                        CommonUtils.showLongToast(R.string.update_user_avatar_fail)
                    } else {
                        setAvatar(result)
                    }
                }
                dismissDialog()
            }

            override fun onError(error: String) {
                dismissDialog()
            }
        })
    }

    fun setAvatar(result: Result<*>?) {
        val users = result?.retData as User
        CommonUtils.showLongToast(R.string.update_user_avatar_success)
        SharePrefrenceUtils.getInstance().userName = users.muserName!!
        KotLinApplication.instance!!.user = users
        var dao = UserDao(this)
        dao.saveUser(users)
        finish()
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    fun gotoClipActivity(uri: Uri) {
        /*if (uri == null) {
            return
        }*/
        var intent: Intent = Intent()
        intent.setClass(this, ClipImageActivity::class.java)
        intent.putExtra("type", type)
        intent.data = uri
        startActivityForResult(intent, REQUEST_CROP_PHOTO)
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */

    fun getRealFilePathFromUri(context: Context, uri: Uri): String {
        var scheme = uri.scheme
        var data = ""
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            var cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (cursor != null) {
                var index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
        return data
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog()
    }

}
