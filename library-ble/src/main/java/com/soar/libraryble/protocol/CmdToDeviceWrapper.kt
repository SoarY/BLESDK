package com.soar.libraryble.protocol

import com.soar.libraryble.constant.EVT_TYPE_ALERT_FIND_WATCH
import com.soar.libraryble.constant.EVT_TYPE_FIRMWARE_VER
import com.soar.libraryble.constant.EVT_TYPE_PHONE_SYSTEM_TYPE
import com.soar.libraryble.protocol.cmd.CmdMergeImpl
import com.soar.libraryble.protocol.imp.ICmdToDeviceWrapper
import com.soar.libraryble.protocol.manger.BleManger
import io.reactivex.Observable
import io.reactivex.Observer
import java.util.function.Consumer

/**
 * NAME：YONG_
 * Created at: 2023/6/19 10
 * Describe:
 */
class CmdToDeviceWrapper private constructor(): ICmdToDeviceWrapper {

    companion object {
        @Volatile
        private var instance: CmdToDeviceWrapper? = null

        fun getInstance(): CmdToDeviceWrapper =
            instance ?: synchronized(this) {
                instance
                    ?: CmdToDeviceWrapper().also {
                        instance = it
                    }
            }

    }

    override fun findDevice(onNext: Consumer<Int>,onError: Consumer<Int>) {
        val bytes = CmdMergeImpl.findDeviceBytes(EVT_TYPE_ALERT_FIND_WATCH)
        BleManger.getInstance().writeData(bytes) {
            if (it == 0)
                onNext.accept(it)
            else
                onError.accept(it)
        }
    }

    override fun setPhoneType() {
        val bytes = CmdMergeImpl.setPhoneType(EVT_TYPE_PHONE_SYSTEM_TYPE)
        BleManger.getInstance().writeData(bytes)
    }

    override fun getFirmwareVer(): Observable<String> {
        val bytes = CmdMergeImpl.getFirmwareVer(EVT_TYPE_FIRMWARE_VER)
        BleManger.getInstance().writeData(bytes)
        return Observable.create{
            ReadToDeviceWrapper.getInstance().firmwareVerObserver= it
        }
    }
}