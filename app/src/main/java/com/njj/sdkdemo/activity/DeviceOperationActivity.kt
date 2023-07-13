package com.njj.sdkdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.njj.sdkdemo.R
import com.njj.sdkdemo.BR
import com.njj.sdkdemo.base.BaseActivity
import com.njj.sdkdemo.databinding.ActivityDeviceBinding
import com.njj.sdkdemo.helper.DeviceControlHelper
import com.njj.sdkdemo.vm.DeviceOperationViewModel

class DeviceOperationActivity : BaseActivity<ActivityDeviceBinding, DeviceOperationViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_device
    }

    override fun initVariableId(): Int {
        return BR.vm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }


    private fun initView() {
        mViewModel.adapter.setOnTagClickListener {position, bean ->
            DeviceControlHelper().userClickItem(bean,position,mDinding.tvLog)
            true
        }

        mDinding.bt2.setOnClickListener {
            OTAActivity.open(context)
        }
    }

    private fun initData() {
        mViewModel.getDeviceOperation()
    }

    companion object{
        fun open(activity:Activity){
            val intent = Intent(activity,DeviceOperationActivity::class.java)
            activity.startActivity(intent)
        }
    }
}