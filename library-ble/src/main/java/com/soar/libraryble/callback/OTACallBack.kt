package com.soar.libraryble.callback

/**
 * NAME：YONG_
 * Created at: 2023/7/13 17
 * Describe:
 */
interface OTACallBack {
    fun onSuccess()

    fun onError(errorType: Int, errorCode: Int)

    fun onProgressChanged(progress: Int)
}