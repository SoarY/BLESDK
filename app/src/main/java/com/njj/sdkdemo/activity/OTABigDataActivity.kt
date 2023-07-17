package com.njj.sdkdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.njj.sdkdemo.R
import com.njj.sdkdemo.base.BaseActivity
import com.njj.sdkdemo.base.BaseViewModel
import com.njj.sdkdemo.databinding.ActivityOtaBigDataBinding
import com.njj.sdkdemo.databinding.ActivityOtaBinding
import com.njj.sdkdemo.helper.PushDataHelper
import com.realsil.sdk.dfu.DfuConstants
import com.realsil.sdk.dfu.model.DfuProgressInfo
import com.realsil.sdk.dfu.model.Throughput
import com.realsil.sdk.dfu.utils.DfuAdapter
import com.soar.cloud.util.FileUtils
import com.soar.cloud.util.ToastUtils
import com.soar.libraryble.callback.OTACallBack
import com.soar.libraryble.protocol.manger.BleManger
import com.soar.libraryble.protocol.manger.RYOTAManger

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
        PushDataHelper().startPush(path!!)
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