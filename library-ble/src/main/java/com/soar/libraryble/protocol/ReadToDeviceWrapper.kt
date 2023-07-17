package com.soar.libraryble.protocol

import android.util.Log
import com.inuker.bluetooth.library.Code
import com.inuker.bluetooth.library.Constants.STATUS_CONNECTED
import com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse
import com.soar.libraryble.callback.CallBackManager
import com.soar.libraryble.constant.EVT_TYPE_FIRMWARE_VER
import com.soar.libraryble.constant.EVT_TYPE_OTA_END
import com.soar.libraryble.constant.EVT_TYPE_OTA_START
import com.soar.libraryble.protocol.cmd.CmdAnalysisImpl
import com.soar.libraryble.protocol.manger.BleManger
import io.reactivex.ObservableEmitter
import java.util.*

/**
 * NAME：YONG_
 * Created at: 2023/6/19 15
 * Describe:
 */
class ReadToDeviceWrapper private constructor(){

    val TAG = ReadToDeviceWrapper.javaClass.simpleName

    var firmwareVerObserver: ObservableEmitter<String>?=null

    var sendBigDataObserver: ObservableEmitter<Boolean>?=null

    var endBigDataObserver: ObservableEmitter<Boolean>?=null

    companion object {
        @Volatile
        private var instance: ReadToDeviceWrapper? = null

        fun getInstance(): ReadToDeviceWrapper =
            instance ?: synchronized(this) {
                instance
                    ?: ReadToDeviceWrapper().also {
                        instance = it
                    }
            }

    }

    val statusChangeListener=object : BleConnectStatusListener() {

        override fun onConnectStatusChanged(mac: String, status: Int) {
            if (status == STATUS_CONNECTED) {
                CallBackManager.getInstance().onConnected(mac)
            } else if (status == STATUS_DISCONNECTED) {
                CallBackManager.getInstance().onDisConnected(mac)
            }
        }

    }

    val notifyResponse =object:BleNotifyResponse{

        override fun onResponse(code: Int) {
            if (code == Code.REQUEST_SUCCESS) {
                CallBackManager.getInstance().onNotificationOpened()

//                val macAddress: String? = BleManger.getInstance().bleDevice.device?.address
//                BleManger.getInstance().requestMtu(macAddress, 512) { i, integer ->
//                    CallBackManager.getInstance().onDiscoveredServices(code)
//                }

                CmdToDeviceWrapper.getInstance().setPhoneType()
            } else {
                BleManger.getInstance().disConnection()
            }
        }

        override fun onNotify(service: UUID, character: UUID, value: ByteArray) {
            handleReceiveData(value)
        }

    }

    private fun handleReceiveData(value: ByteArray) {
        val cmd = value[1].toInt() and (0xff)
        Log.i(TAG, "handleReceiveData: cmd:$cmd")
        when(cmd){
            EVT_TYPE_FIRMWARE_VER->{
                val firmwareVer = CmdAnalysisImpl.parserFirmware(value)
                firmwareVerObserver?.onNext(firmwareVer)
            }
            EVT_TYPE_OTA_START->{
                sendBigDataObserver?.onNext(true)
            }
            EVT_TYPE_OTA_END->{
                val b = CmdAnalysisImpl.parserEndBigData(value)
                endBigDataObserver?.onNext(b)
            }
        }


    }

}