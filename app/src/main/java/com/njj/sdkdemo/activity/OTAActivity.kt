package com.njj.sdkdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.njj.sdkdemo.R
import com.njj.sdkdemo.base.BaseActivity
import com.njj.sdkdemo.base.BaseViewModel
import com.njj.sdkdemo.databinding.ActivityOtaBinding
import com.realsil.sdk.dfu.DfuConstants
import com.realsil.sdk.dfu.model.DfuProgressInfo
import com.realsil.sdk.dfu.model.Throughput
import com.realsil.sdk.dfu.utils.DfuAdapter
import com.soar.cloud.util.FileUtils
import com.soar.cloud.util.ToastUtils
import com.soar.libraryble.callback.OTACallBack
import com.soar.libraryble.protocol.manger.BleManger
import com.soar.libraryble.protocol.manger.RYOTAManger

class OTAActivity : BaseActivity<ActivityOtaBinding, BaseViewModel>() {

    private var path:String?=null

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_ota
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

        mDinding.btStartOta.setOnClickListener {
            startOta()
        }
    }

    private fun selectFile() {
        // 打开文件管理器选择文件
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" //无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_SELECT_FILE)
    }

    private fun startOta() {
        if (path==null){
            ToastUtils.showToast("请选择Ota升级文件")
            return
        }

        val mac = BleManger.getInstance().bleDevice.device!!.address
        RYOTAManger.getInstance().dfu(mac,path!!,object: OTACallBack{
            override fun onSuccess() {
                Log.i(TAG, "onSuccess: ")
            }

            override fun onError(errorType: Int, errorCode: Int) {
                Log.i(TAG, "onError:  errorType:$errorType errorCode:$errorCode")
            }

            override fun onProgressChanged(progress: Int) {
                Log.i(TAG, "onProgressChanged:  progress:$progress")
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_FILE) {
            if (data==null||data.data==null)
                return
            val uri = data.data
            val file = FileUtils.uriToFile(this, uri)
            path = file?.path
        }
    }

    companion object{
        private const val REQUEST_SELECT_FILE = 100

        fun open(activity:Activity){
            val intent = Intent(activity,OTAActivity::class.java)
            activity.startActivity(intent)
        }
    }
}