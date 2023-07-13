package com.soar.libraryble.protocol.manger

import android.content.Context
import com.realsil.sdk.core.RtkConfigure
import com.realsil.sdk.core.RtkCore
import com.realsil.sdk.dfu.DfuConstants
import com.realsil.sdk.dfu.RtkDfu
import com.realsil.sdk.dfu.model.DfuConfig
import com.realsil.sdk.dfu.model.DfuProgressInfo
import com.realsil.sdk.dfu.model.OtaDeviceInfo
import com.realsil.sdk.dfu.model.Throughput
import com.realsil.sdk.dfu.utils.DfuAdapter
import com.realsil.sdk.dfu.utils.GattDfuAdapter
import com.soar.libraryble.callback.OTACallBack

/**
 * NAME：YONG_
 * Created at: 2023/7/13 15
 * Describe:
 */
class RYOTAManger private constructor(){

    private val isDebug = true

    private lateinit var context: Context
    private lateinit var gattDfuAdapter:GattDfuAdapter
    private var callback:OTACallBack?=null

    companion object {
        @Volatile
        private var instance: RYOTAManger? = null

        fun getInstance(): RYOTAManger =
            instance ?: synchronized(this) {
                instance
                    ?: RYOTAManger().also {
                        instance = it
                    }
            }
    }

    fun init(context: Context){
        this.context=context

        val configure: RtkConfigure = RtkConfigure.Builder().debugEnabled(isDebug)
            .printLog(isDebug)
            .logTag("OTA")
            .build()
        RtkCore.initialize(context, configure)
        RtkDfu.initialize(context, isDebug)

        gattDfuAdapter = GattDfuAdapter.getInstance(context)
        gattDfuAdapter.initialize(dfuCallback)
    }

   private val dfuCallback = object : DfuAdapter.DfuHelperCallback() {
        override fun onStateChanged(state: Int) {
            super.onStateChanged(state)
            if (state == DfuAdapter.STATE_DISCONNECTED || state == DfuAdapter.STATE_CONNECT_FAILED) {
                callback?.onError(21,21)
            }
        }

        override fun onError(type: Int, code: Int) {
            super.onError(type, code)
            callback?.onError(type,code)
        }

        override fun onProcessStateChanged(state: Int, p1: Throughput?) {
            super.onProcessStateChanged(state, p1)
            if (state == DfuConstants.PROGRESS_IMAGE_ACTIVE_SUCCESS) {
                callback?.onSuccess()
            } else if (state == DfuConstants.STATE_DISCOVER_SERVICE) {
                callback?.onProgressChanged(0)
            }
        }

        override fun onProgressChanged(p0: DfuProgressInfo?) {
            super.onProgressChanged(p0)
            if (p0==null)
                return
            callback?.onProgressChanged(p0.progress)
        }
    }

    fun dfu(mac:String,path:String,callback: OTACallBack){
        this.callback=callback

        var mDfuConfig = DfuConfig()
        mDfuConfig.otaWorkMode = gattDfuAdapter.getPriorityWorkMode(DfuConstants.OTA_MODE_SILENT_FUNCTION).workmode
        mDfuConfig.channelType = DfuConfig.CHANNEL_TYPE_GATT
        mDfuConfig.isVersionCheckEnabled = false
        mDfuConfig.address = mac
        mDfuConfig.filePath = path

        gattDfuAdapter.startOtaProcedure(OtaDeviceInfo(), mDfuConfig)
    }
}