package com.njj.sdkdemo.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.soar.cloud.util.FileUtils
import com.soar.libraryble.protocol.CmdToDeviceWrapper
import com.soar.libraryble.protocol.ReadToDeviceWrapper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * NAME：YONG_
 * Created at: 2023/7/14 14
 * Describe:
 */
class PushDataHelper {

    val TAG = this.javaClass.simpleName

    private val chunkLength = 220
    // 定义延迟时间（20毫秒）
    private val delayTime = 20L

    @SuppressLint("CheckResult")
    fun startPush(path:String){
        Observable.create<ByteArray> {
            var buffer = FileUtils.inputFile(path)
            it.onNext(buffer)
        }.subscribeOn(Schedulers.io())
            .subscribe {
                var buffer=it
                CmdToDeviceWrapper.getInstance().sendBigData(1,buffer!!.size,chunkLength).subscribe{
                    val splitByteArray = splitByteArray(buffer)
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
        // 调用扩展函数，并传入循环处理的操作
        val disposable = splitByteArray.loopWithDelay { index, byteArray ->
            CmdToDeviceWrapper.getInstance().exSendBigData(byteArray,index).subscribe {
                when (it) {
                    0 -> Log.i(TAG,"sendBigData success")
                    1 -> {

                    }else -> {
                    Log.i(TAG,"sendBigData fail")
                }
                }
            }
        }

        //disposable.dispose()
    }

    // 扩展函数：循环处理MutableList中的ByteArray并添加延迟
    fun MutableList<ByteArray>.loopWithDelay(action: (index: Int, byteArray: ByteArray) -> Unit): Disposable {
        // 使用intervalRange操作符创建一个定时器，初始延迟为0，每隔delayTime毫秒发射一个递增的长整型数，总共发射size次
        return Observable.intervalRange(0, size.toLong(), 0, delayTime, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io()) // 将处理转移到IO线程执行
            .subscribe { index ->
                if (index < size) {
                    val byteArray = this[index.toInt()]
                    // 对每个ByteArray元素执行操作，同时传递索引
                    action(index.toInt(), byteArray)
                }
            }
    }
}