package com.soar.libraryble.callback

/**
 * NAME：YONG_
 * Created at: 2023/7/13 17
 * Describe:
 */
interface OTABigDataCallBack {

    fun onSuccess()

    fun onRepair(packId: Int)

    fun onError(errorCode: Int)

    fun onProgress(progress: Int)
}