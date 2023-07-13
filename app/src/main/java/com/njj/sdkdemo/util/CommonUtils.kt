package com.soar.cloud.util

import com.njj.sdkdemo.MyApplication

/**
 * NAME：YONG_
 * Created at: 2019/1/7
 * Describe:
 */
object CommonUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(dpValue: Float): Int {
        val scale: Float = MyApplication.context.getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }
}