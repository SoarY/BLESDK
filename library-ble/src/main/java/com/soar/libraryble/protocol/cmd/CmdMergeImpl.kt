package com.soar.libraryble.protocol.cmd

import com.soar.libraryble.constant.BLE_CTRL_READ
import com.soar.libraryble.constant.BLE_CTRL_WRITE

/**
 * NAME：YONG_
 * Created at: 2023/6/19 11
 * Describe:
 */
object CmdMergeImpl {

    fun createBaseCmdByte(
        cmdLength: Int,
        cmdType: Int,
        controlType: Int,
        dataLength: Int
    ): ByteArray {
        val bytes = ByteArray(cmdLength)
        bytes[0] = 0xBC.toByte()
        bytes[1] = cmdType.toByte()
        bytes[2] = controlType.toByte()
        bytes[3] = dataLength.toByte()
        return bytes
    }

    fun findDeviceBytes(cmdType:Int):ByteArray{
        val bytes = createBaseCmdByte(6, cmdType, BLE_CTRL_WRITE, 1)
        bytes[4] = 0
        bytes[5] = 0
        return bytes
    }

    fun setPhoneType(cmdType:Int):ByteArray{
        val bytes = createBaseCmdByte(6, cmdType, BLE_CTRL_WRITE, 1)
        bytes[4] = 2
        bytes[5] = bytes[4]
        return bytes
    }

    fun getFirmwareVer(cmdType:Int):ByteArray{
        val bytes = createBaseCmdByte(6, cmdType, BLE_CTRL_READ, 1)
        bytes[4] = 0
        bytes[5] = 0
        return bytes
    }

}