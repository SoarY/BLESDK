package com.njj.sdkdemo.vm

import android.app.Application
import android.text.TextUtils
import android.util.Log
import com.njj.sdkdemo.adapter.DeviceAdapter
import com.njj.sdkdemo.base.BaseViewModel
import com.soar.libraryble.callback.SearchCallBack
import com.soar.libraryble.entity.ABLEDevice
import com.soar.libraryble.protocol.manger.BleManger
import java.util.ArrayList

/**
 * NAME：YONG_
 * Created at: 2023/4/6 15
 * Describe:
 */
class MainViewModel(application:Application) : BaseViewModel(application){

    val TAG=this.javaClass.simpleName

    private val dataList =ArrayList<ABLEDevice>()

    var adapter = DeviceAdapter()

    fun scan() {
        BleManger.getInstance().scanBluetooth(object : SearchCallBack {

            override fun onSearchStarted() {
                Log.i(TAG,"onSearchStarted")
            }

            override fun onDeviceFounded(device: ABLEDevice) {
                Log.i(TAG,device.device?.address+"")

                if (device.device==null)
                    return

                if (dataList.isEmpty()){
                    dataList.add(device)
                }else{
                    val isAnyMatched = dataList.any {
                        !TextUtils.isEmpty(it.device?.address)&&it.device?.address.equals(device.device?.address)
                    }
                    if (!isAnyMatched)
                        dataList.add(device)
                }

                //排序
                dataList.sort()
                adapter.setData(dataList)
            }

            override fun onSearchStopped() {
                Log.i(TAG,"onSearchStopped")

            }

            override fun onSearchCanceled() {
                Log.i(TAG,"onSearchCanceled")
            }

        })
    }

}