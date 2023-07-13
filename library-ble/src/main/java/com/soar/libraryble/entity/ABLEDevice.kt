package com.soar.libraryble.entity

import android.bluetooth.BluetoothDevice
import java.io.Serializable

/**
 * NAME：YONG_
 * Created at: 2023/6/19 17
 * Describe:
 */
class ABLEDevice: Serializable, Comparable<ABLEDevice> {
    var device //蓝牙
            : BluetoothDevice? = null
    var projectNo //适配号
            : String? = null
    var rssi //信号值
            = 0
    var scanRecord //广播数据
            : ByteArray?=null

    override fun compareTo(o: ABLEDevice): Int {
        return Integer.compare(o.rssi, rssi)
    }

    override fun toString(): String {
        return "BLEDevice{" +
                ", deviceRssi=" + rssi + '}'
    }
}