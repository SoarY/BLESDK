package com.njj.sdkdemo.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import com.soar.cloud.util.FileUtils
import com.soar.libraryble.protocol.CmdToDeviceWrapper
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * NAME：YONG_
 * Created at: 2023/7/14 14
 * Describe:
 */
class PushDataHelper {

    var buffer:ByteArray?=null
    val chunkLength = 230

    @SuppressLint("CheckResult")
    fun startPush(path:String){
        Observable.create<Boolean> {
            buffer = FileUtils.inputFile(path)
            it.onNext(true)
        }.subscribeOn(Schedulers.io())
            .subscribe {
                CmdToDeviceWrapper.getInstance().sendBigData(1,buffer!!.size,chunkLength).subscribe{
                    val splitByteArray = splitByteArray(buffer!!)
                    exSendBigData(splitByteArray)
                }
            }
    }

    private fun splitByteArray(buffer: ByteArray): MutableList<ByteArray> {
        val originalLength: Int = buffer.size

        var numOfChunks = originalLength / chunkLength
        if (originalLength % chunkLength != 0)
            numOfChunks += 1

        val dividedList: MutableList<ByteArray> = ArrayList()

        for (i in 0 until numOfChunks) {
            val startIndex: Int = i * chunkLength
            val endIndex = Math.min(startIndex + chunkLength, originalLength)
            val chunk: ByteArray = Arrays.copyOfRange(buffer, startIndex, endIndex)
            dividedList.add(chunk)
        }
        return dividedList
    }

    private fun exSendBigData(splitByteArray: MutableList<ByteArray>) {
        //开始循环发送数据包
        splitByteArray.forEachIndexed { index, bytes ->
            Timer().schedule(object :TimerTask(){
                override fun run() {
                    CmdToDeviceWrapper.getInstance().exSendBigData(bytes,index)
                }
            },20)
        }
    }
}