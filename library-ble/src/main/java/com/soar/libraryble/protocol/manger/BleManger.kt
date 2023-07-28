package com.soar.libraryble.protocol.manger

import android.content.Context
import com.inuker.bluetooth.library.BluetoothClient
import com.inuker.bluetooth.library.Code
import com.inuker.bluetooth.library.Constants
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener
import com.inuker.bluetooth.library.connect.options.BleConnectOptions
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse
import com.inuker.bluetooth.library.connect.response.BleWriteResponse
import com.inuker.bluetooth.library.search.SearchRequest
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.search.response.SearchResponse
import com.soar.libraryble.callback.SearchCallBack
import com.soar.libraryble.constant.MUUIDConfig
import com.soar.libraryble.entity.ABLEDevice
import com.soar.libraryble.protocol.ReadToDeviceWrapper

/**
 * NAME：YONG_
 * Created at: 2023/6/19 11
 * Describe:
 */
class BleManger private constructor(){

    lateinit var bleDevice: ABLEDevice

    lateinit var bluetoothClient: BluetoothClient

    companion object {
        @Volatile
        private var instance: BleManger? = null

        fun getInstance(): BleManger =
            instance ?: synchronized(this) {
                instance
                    ?: BleManger().also {
                        instance = it
                    }
            }
    }

    fun init(context:Context){
        //final变量无法修改
        // 修改蓝牙库最大缓存队列
        //PrivateStaticFieldModifier.setPrivateStaticFieldValue(BleConnectDispatcher::class.java,"MAX_REQUEST_COUNT",90000)
        bluetoothClient = BluetoothClient(context)
    }

    fun isConnect(): Boolean {
        if (bleDevice == null) {
            return false
        }
        return isConnect(bleDevice.device!!.address)
    }

    fun isConnect(mac: String): Boolean {
        val status: Int = bluetoothClient.getConnectStatus(mac)
        return Constants.STATUS_DEVICE_CONNECTED == status
    }

    fun scanBluetooth(searchCallback: SearchCallBack?){
        val request = SearchRequest.Builder()
            .searchBluetoothLeDevice(1000 * 20, 1) // 先扫BLE设备1次，每次20s
            .build()
        bluetoothClient.search(request,object: SearchResponse {
            override fun onSearchStarted() {
                searchCallback?.onSearchStarted()
            }

            override fun onDeviceFounded(device: SearchResult?) {
                val bleDevice = ABLEDevice()
                bleDevice.rssi==device?.rssi
                bleDevice.scanRecord=device?.scanRecord
                bleDevice.device=device?.device

                searchCallback?.onDeviceFounded(bleDevice)
            }

            override fun onSearchStopped() {
                searchCallback?.onSearchStopped()
            }

            override fun onSearchCanceled() {
                searchCallback?.onSearchCanceled()
            }

        })
    }

    fun disConnection() {
        bluetoothClient.disconnect(bleDevice.device?.address)
    }

    /**
     * 停止扫描
     */
    fun scanStop() {
        bluetoothClient.stopSearch()
    }

    fun connectionRequest(bleDevice: ABLEDevice){
        var options = BleConnectOptions.Builder()
            .setConnectRetry(0) // 连接如果失败重试1次
            .setConnectTimeout(30000) // 连接超时30s
            .setServiceDiscoverRetry(3) // 发现服务如果失败重试3次
            .setServiceDiscoverTimeout(20000) // 发现服务超时20s
            .build()
        bluetoothClient.connect(bleDevice.device!!.address,options) { code, data ->
            if (code == Code.REQUEST_SUCCESS) {
                this.bleDevice = bleDevice
                openNotify(bleDevice.device!!.address, ReadToDeviceWrapper.getInstance().notifyResponse)
            }
        }

        registerConnection(bleDevice.device!!.address,ReadToDeviceWrapper.getInstance().statusChangeListener)
    }

    fun openNotify(mac: String, bleNotifyResponse: BleNotifyResponse) {
        bluetoothClient.notify(
            mac,
            MUUIDConfig.SERVICE_UUID_NJY,
            MUUIDConfig.NOTIFY_UUID_NJY,
            bleNotifyResponse
        )
    }

    /**
     * 注册连接监听
     */
    fun registerConnection(mac: String, mBleConnectStatusListener: BleConnectStatusListener?) {
        bluetoothClient.registerConnectStatusListener(mac, mBleConnectStatusListener)
    }

    fun writeData(cmd:ByteArray){
        if (!isConnect()) {
            return
        }
        writeCommand(bleDevice.device!!.address, cmd) { }
    }

    fun writeData(cmd:ByteArray,response: BleWriteResponse?){
        if (!isConnect()) {
            return
        }
        writeCommand(bleDevice.device!!.address, cmd, response)
    }

    fun writeCommand(mac: String, cmd: ByteArray, response: BleWriteResponse?) {
        val serviceUUid = MUUIDConfig.SERVICE_UUID_NJY
        val writeUUid = MUUIDConfig.WRITE_UUID_NJY
        bluetoothClient.writeNoRsp(
            mac, serviceUUid, writeUUid,
            cmd, response
        )
    }
}