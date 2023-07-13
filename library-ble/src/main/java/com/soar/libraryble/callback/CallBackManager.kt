package com.soar.libraryble.callback

/**
 * NAME：YONG_
 * Created at: 2023/6/19 16
 * Describe:
 */
class CallBackManager private constructor(){

    val mIConnectStateCallBackList: ArrayList<IConnectStateCallBack> = ArrayList()

    interface IConnectStateCallBack {
        /**
         * 连接
         */
        fun onConnected(mac: String?)

        /**
         * 正在连接
         */
        fun onConnecting(mac: String?)

        /**
         * 连接断开
         */
        fun onDisConnected(mac: String?)

        /**
         * 通知已打开
         */
        fun onNotificationOpened()
    }

    companion object {
        @Volatile
        private var instance: CallBackManager? = null

        fun getInstance(): CallBackManager =
            instance ?: synchronized(this) {
                instance
                    ?: CallBackManager().also {
                        instance = it
                    }
            }
    }

    fun registerConnectStatuesCallBack(callBack: IConnectStateCallBack?) {
        if (!mIConnectStateCallBackList.contains(callBack) && callBack != null) {
            mIConnectStateCallBackList.add(callBack)
        }
    }

    fun unregisterConnectStatuesCallBack(callBack: IConnectStateCallBack?) {
        if (mIConnectStateCallBackList.contains(callBack) && callBack != null) {
            mIConnectStateCallBackList.remove(callBack)
        }
    }

    /**
     * 已经连接
     */
    fun onConnected(mac: String?) {
        for (callBack in mIConnectStateCallBackList) {
            callBack.onConnected(mac)
        }
    }


    /**
     * 已经断开连接
     */
    fun onDisConnected(mac: String?) {
        for (callBack in mIConnectStateCallBackList) {
            callBack.onDisConnected(mac)
        }
    }

    /**
     * 正在连接
     */
    fun onConnecting(mac: String?) {
        for (callBack in mIConnectStateCallBackList) {
            callBack.onConnecting(mac)
        }
    }

    /**
     * 通知已打开
     */
    fun onNotificationOpened() {
        for (callBack in mIConnectStateCallBackList) {
            callBack.onNotificationOpened()
        }
    }
}