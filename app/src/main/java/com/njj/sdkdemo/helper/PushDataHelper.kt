package com.njj.sdkdemo.helper

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.njj.sdkdemo.util.FileImgUtils
import com.soar.cloud.util.FileUtils
import com.soar.libraryble.callback.OTABigDataCallBack
import com.soar.libraryble.protocol.CmdToDeviceWrapper
import com.soar.libraryble.protocol.ReadToDeviceWrapper
import com.soar.libraryble.protocol.cmd.CmdMergeImpl
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.nio.ByteBuffer
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
    // 定义延迟时间
    private val DELAY_TIME = 60L

    private var disposable:Disposable?=null

    @SuppressLint("CheckResult")
    fun pushDial(path:String,lcdWidth:Int,lcdHeight:Int,previewWidth:Int,previewHeight:Int,timePosition:Int,colors:String,
                 otaBigDataCallBack: OTABigDataCallBack?){
        Observable.create<ByteArray> {
            var buffer=dialBuffer(path, lcdWidth, lcdHeight, previewWidth, previewHeight, timePosition, colors)
            it.onNext(buffer)
        }.subscribeOn(Schedulers.io())
            .subscribe({
                var buffer=it
                CmdToDeviceWrapper.getInstance().sendBigData(1,buffer!!.size,chunkLength).subscribe{
                    val splitByteArray = splitByteArray(buffer)
                    disposable = exSendBigData(0, splitByteArray, otaBigDataCallBack)
                    setOtaBigDataCallBack(otaBigDataCallBack,splitByteArray)
                }
            }, {
                otaBigDataCallBack?.onError(-1)
            })
    }

    private fun dialBuffer(path: String, lcdWidth: Int, lcdHeight: Int, previewWidth: Int, previewHeight: Int, timePosition: Int, colors: String): ByteArray {
        val mBitmap = BitmapFactory.decodeFile(path)

        val bitmap = FileImgUtils.getSmallBitmap(mBitmap, lcdWidth, lcdHeight)
        val smallBitmap = FileImgUtils.getSmallBitmap(mBitmap, previewWidth, previewHeight)

        val bg = getBufferByBitmap(bitmap!!)
        val smallBg = getBufferByBitmap(smallBitmap!!)


        var buffer = ByteArray(16 + bg!!.size + smallBg!!.size)
        val defaultByte: ByteArray = CmdMergeImpl.getDialByte(
            timePosition,
            colors,
            lcdWidth,
            lcdHeight,
            previewWidth,
            previewHeight
        )

        System.arraycopy(defaultByte, 0, buffer, 0, defaultByte.size)
        System.arraycopy(bg, 0, buffer, defaultByte.size, bg.size)
        System.arraycopy(smallBg, 0, buffer, defaultByte.size + bg.size, smallBg.size)
        return buffer
    }

    private fun getBufferByBitmap(bmap: Bitmap): ByteArray? {
        val bytes = bmap.byteCount
        val buffer = ByteBuffer.allocate(bytes)
        bmap.copyPixelsToBuffer(buffer)
        return changeArrayIndex(buffer.array())
    }

    private fun changeArrayIndex(bytes: ByteArray): ByteArray? {
        var des: Byte
        var i = 0
        while (i < bytes.size) {
            des = bytes[i]
            bytes[i] = bytes[i + 1]
            bytes[i + 1] = des
            i += 2
        }
        return bytes
    }

    @SuppressLint("CheckResult")
    fun pushDialFile(path:String, otaBigDataCallBack: OTABigDataCallBack?){
        Observable.create<ByteArray> {
            var buffer = FileUtils.inputFile(path)
            it.onNext(buffer)
        }.subscribeOn(Schedulers.io())
            .subscribe({
                var buffer=it
                CmdToDeviceWrapper.getInstance().sendBigData(1,buffer!!.size,chunkLength).subscribe{
                    val splitByteArray = splitByteArray(buffer)
                    disposable = exSendBigData(0, splitByteArray, otaBigDataCallBack)
                    setOtaBigDataCallBack(otaBigDataCallBack,splitByteArray)
                }
            }, {
                otaBigDataCallBack?.onError(-1)
            })
    }

    private fun setOtaBigDataCallBack(
        otaBigDataCallBack: OTABigDataCallBack?,
        splitByteArray: MutableList<ByteArray>
    ) {
        ReadToDeviceWrapper.getInstance().otaBigDataCallBack = object : OTABigDataCallBack {
            override fun onSuccess() {
                disposable?.dispose()
                otaBigDataCallBack?.onSuccess()
            }

            override fun onRepair(packId: Int) {
                disposable?.dispose()
                val startIndex = packId + 1
                disposable = exSendBigData(startIndex.toLong(),splitByteArray,otaBigDataCallBack)
                otaBigDataCallBack?.onRepair(startIndex)
            }

            override fun onError(errorCode: Int) {
                disposable?.dispose()
                otaBigDataCallBack?.onError(errorCode)
            }

            override fun onProgress(progress: Int) {
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

    private fun exSendBigData(
        startIndex: Long,
        splitByteArray: MutableList<ByteArray>,
        otaBigDataCallBack: OTABigDataCallBack?
    ): Disposable {
        // 调用扩展函数，并传入循环处理的操作
        return splitByteArray.loopWithDelay(startIndex) { index, byteArray ->
            CmdToDeviceWrapper.getInstance().exSendBigData(byteArray,index){
                if (splitByteArray.size!=0){
                    val progress = (((index+1).toFloat() / splitByteArray.size)*100).toInt()
                    otaBigDataCallBack?.onProgress(progress)
                }
            }
        }
    }

    // 扩展函数：循环处理MutableList中的ByteArray并添加延迟
    fun MutableList<ByteArray>.loopWithDelay(startIndex: Long,action: (index: Int, byteArray: ByteArray) -> Unit): Disposable {
        // 使用intervalRange操作符创建一个定时器，初始延迟为0，每隔delayTime毫秒发射一个递增的长整型数，总共发射size次
        return Observable.intervalRange(startIndex, size.toLong()-startIndex, 0, DELAY_TIME, TimeUnit.MILLISECONDS)
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