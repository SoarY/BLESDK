package com.njj.sdkdemo.activity

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.njj.sdkdemo.R
import com.njj.sdkdemo.base.BaseActivity
import com.njj.sdkdemo.base.BaseViewModel
import com.njj.sdkdemo.databinding.ActivityDialCustomBinding
import com.njj.sdkdemo.databinding.ActivityOtaBinding
import com.njj.sdkdemo.helper.PushDataHelper
import com.njj.sdkdemo.util.FileImgUtils
import com.soar.cloud.util.FileUtils
import com.soar.cloud.util.ToastUtils
import com.soar.libraryble.callback.OTABigDataCallBack
import com.soar.libraryble.callback.OTACallBack
import com.soar.libraryble.protocol.CmdToDeviceWrapper
import com.soar.libraryble.protocol.manger.BleManger
import com.soar.libraryble.protocol.manger.RYOTAManger
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File

class DialCustomActivity : BaseActivity<ActivityDialCustomBinding, BaseViewModel>() {

    private var imageCropFile: File?=null

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_dial_custom
    }

    override fun initVariableId(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }


    private fun initView() {
        mDinding.btSelectFile.setOnClickListener {
            selectFile()
        }

        mDinding.btPush.setOnClickListener {
            startPush()
        }
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_ALBUM)
    }

    private fun startPush() {
        if (imageCropFile==null){
            ToastUtils.showToast("请选择文件")
            return
        }

        PushDataHelper().pushDial(imageCropFile!!.absolutePath,320,380,240,283,0,toHexEncoding(getColor(R.color.white)),
            object : OTABigDataCallBack{
                override fun onSuccess() {
                    Log.i(TAG, "onSuccess: ")
                }

                override fun onRepair(packId: Int) {
                    Log.i(TAG, "onRepair: "+packId)
                }

                override fun onError(errorCode: Int) {
                    Log.i(TAG, "onError: "+errorCode)
                }

                override fun onProgress(progress: Int) {
                    Log.i(TAG, "onProgress: "+progress)
                }
            })
    }

    fun toHexEncoding(color: Int): String {
        val sb = StringBuffer()
        var R: String = Integer.toHexString(Color.red(color))
        var G: String = Integer.toHexString(Color.green(color))
        var B: String = Integer.toHexString(Color.blue(color))
        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = if (R.length == 1) "0$R" else R
        G = if (G.length == 1) "0$G" else G
        B = if (B.length == 1) "0$B" else B
        sb.append(R)
        sb.append(G)
        sb.append(B)
        return sb.toString()
    }

    private fun gotoCrop(sourceUri: Uri) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX",320) //X方向上的比例
        intent.putExtra("aspectY", 380) //Y方向上的比例
        intent.putExtra("outputX", 320) //裁剪区的宽
        intent.putExtra("outputY",380) //裁剪区的高
        intent.putExtra("scale ", true) //是否保留比例
        intent.putExtra("return-data", false)
        intent.putExtra("outputFormat", "png")
        intent.setDataAndType(sourceUri, "image/*") //设置数据源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val uri = FileImgUtils.createImageUri(this, true)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        } else {
            val imageCropFile = FileImgUtils.createImageFile(this, true)
            val imgCropUri: Uri = Uri.fromFile(imageCropFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCropUri)
        }
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CROP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data==null||data.data==null)
            return
        val uri = data.data
        if (requestCode == REQUEST_CODE_ALBUM) {
            gotoCrop(uri!!)
        }else if (requestCode == REQUEST_CODE_CAPTURE_CROP) {
            imageCropFile = FileImgUtils.getUriToFile(this, uri)
            //转成RGB565的16位位图
            val smallBitmap = FileImgUtils.getSmallBitmap(
                BitmapFactory.decodeFile(imageCropFile!!.absoluteFile.toString()),
                320,380
            )
            //FileImgUtils.saveBitmap(smallBitmap!!, FileImgUtils.getCustomDialBgPath())

            mDinding.ivImg.setImageBitmap(smallBitmap)
        }
    }

    companion object{
        private const val REQUEST_CODE_ALBUM = 100
        private const val REQUEST_CODE_CAPTURE_CROP = 101

        fun open(activity:Activity){
            val intent = Intent(activity,DialCustomActivity::class.java)
            activity.startActivity(intent)
        }
    }
}