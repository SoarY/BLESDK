package com.njj.sdkdemo.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.njj.sdkdemo.BR
import com.njj.sdkdemo.R
import com.njj.sdkdemo.base.BaseActivity
import com.njj.sdkdemo.databinding.ActivityMainBinding
import com.njj.sdkdemo.utils.permissions.PermissionsUtils
import com.njj.sdkdemo.vm.MainViewModel
import com.soar.libraryble.callback.CallBackManager
import com.soar.libraryble.protocol.manger.BleManger
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.vm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAssembly()
        initView()
    }

    private fun initAssembly() {
        CallBackManager.getInstance().registerConnectStatuesCallBack(callBack)
    }

    private fun initView() {
        mDinding.btSearch.setOnClickListener {
            requestBLEPermission()
        }

        mViewModel.adapter.setItemClickListener{
            Log.i(TAG, "initView: "+it)
            BleManger.getInstance().scanStop()
            BleManger.getInstance().connectionRequest(mViewModel.adapter.datas!![it])
        }
    }

    private fun requestBLEPermission() {
        val permsList =  ArrayList<String>()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            permsList.add(Manifest.permission.BLUETOOTH_SCAN)
            permsList.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            permsList.add(Manifest.permission.BLUETOOTH_CONNECT)
        }else {
            permsList.add(Manifest.permission.BLUETOOTH)
            permsList.add(Manifest.permission.BLUETOOTH_ADMIN)
        }
        permsList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permsList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        val perms = permsList.toTypedArray()
        PermissionsUtils.checkPermission(RxPermissions(this), {
            mViewModel.scan()
        }, null,null, *perms)
    }

    val callBack = object : CallBackManager.IConnectStateCallBack{
        override fun onConnected(mac: String?) {
            Log.i(TAG, "onConnected: "+mac)
        }

        override fun onConnecting(mac: String?) {
            Log.i(TAG, "onConnecting: "+mac)
        }

        override fun onDisConnected(mac: String?) {
            Log.i(TAG, "onDisConnected: "+mac)
        }

        override fun onNotificationOpened() {
            Log.i(TAG, "onNotificationOpened: ")
            DeviceOperationActivity.open(this@MainActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (callBack!=null)
            CallBackManager.getInstance().unregisterConnectStatuesCallBack(callBack)
    }
}