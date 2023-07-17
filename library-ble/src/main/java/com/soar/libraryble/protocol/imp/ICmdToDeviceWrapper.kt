package com.soar.libraryble.protocol.imp

import io.reactivex.Observable
import java.util.function.Consumer

/**
 * NAME：YONG_
 * Created at: 2023/6/19 11
 * Describe:
 */
interface ICmdToDeviceWrapper {
    fun  findDevice(onNext: Consumer<Int>,onError: Consumer<Int>)

    fun  setPhoneType()

    fun  getFirmwareVer(): Observable<String>

    fun  sendBigData(bigDataType:Int,size:Int,chunkLength:Int): Observable<Boolean>

    fun  exSendBigData(data:ByteArray,index: Int): Observable<Boolean>
}