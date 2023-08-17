package com.njj.sdkdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.njj.sdkdemo.R
import com.njj.sdkdemo.base.BaseActivity
import com.njj.sdkdemo.base.BaseViewModel
import com.njj.sdkdemo.databinding.ActivityOtaBigDataBinding
import com.njj.sdkdemo.helper.PushDataHelper
import com.soar.cloud.util.FileUtils
import com.soar.cloud.util.ToastUtils
import com.soar.libraryble.callback.OTABigDataCallBack

class OTABigDataActivity : BaseActivity<ActivityOtaBigDataBinding, BaseViewModel>() {

    private var path:String?=null

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_ota_big_data
    }

    override fun initVariableId(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }


    private fun initView() {
        mDinding.btSelectFile.setOnClickListener { selectFile() }

        mDinding.bt1.setOnClickListener { otaBigData() }
    }

    private fun selectFile() {
        // 打开文件管理器选择文件
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" //无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_SELECT_FILE)
    }

    private fun otaBigData() {
        if (path==null){
            ToastUtils.showToast("请选择文件")
            return
        }

        PushDataHelper().pushDialFile(path!!,object :OTABigDataCallBack{
            override fun onSuccess() {
                Log.i(TAG, "onSuccess: ")
            }

            override fun onRepair(packId: Int) {
                Log.i(TAG, "onRepair: $packId")
            }

            override fun onError(errorCode: Int) {
                Log.i(TAG, "onError: $errorCode")
            }

            override fun onProgress(progress: Int) {
                Log.i(TAG, "onProgress: $progress")
                mDinding.progressBarHorizontal.progress=progress
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
            val intent = Intent(activity,OTABigDataActivity::class.java)
            activity.startActivity(intent)
        }
    }
}