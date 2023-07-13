package com.njj.sdkdemo.helper

import android.annotation.SuppressLint
import android.widget.TextView
import com.njj.sdkdemo.bean.DeviceOperation
import com.soar.libraryble.constant.EVT_TYPE_ALERT_FIND_WATCH
import com.soar.libraryble.constant.EVT_TYPE_FIRMWARE_VER
import com.soar.libraryble.protocol.CmdToDeviceWrapper

/**
 * NAME：YONG_
 * Created at: 2023/6/17 14
 * Describe:
 */
class DeviceControlHelper {

    @SuppressLint("CheckResult")
    fun userClickItem(info: DeviceOperation, pos: Int, text: TextView) {
        when(info.type){
            EVT_TYPE_ALERT_FIND_WATCH -> {
                CmdToDeviceWrapper.getInstance().findDevice(onNext = {
                    text.text = "查找手表成功"
                }, onError = {
                    text.text = "查找手表失败"
                })
            }

            EVT_TYPE_FIRMWARE_VER -> {
                CmdToDeviceWrapper.getInstance().getFirmwareVer().subscribe {
                    text.text=it
                }
            }
        }
    }
}