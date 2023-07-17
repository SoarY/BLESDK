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

    fun sendBigData(cmdType:Int,bigDataType: Int,size: Int,chunkLength: Int):ByteArray{
        val bytes = createBaseCmdByte(12, cmdType, BLE_CTRL_WRITE, 7)
        bytes[4] = bigDataType.toByte()

        bytes[5] = ((size and (0xff))).toByte()
        bytes[6] = ((size.shr(8) and (0xff))).toByte()
        bytes[7] = ((size.shr(16) and (0xff))).toByte()
        bytes[8] = ((size.shr(24) and (0xff))).toByte()

        var packageCount = size / chunkLength
        if (size % chunkLength != 0)
            packageCount += 1

        bytes[9] = ((packageCount and (0xff))).toByte()
        bytes[10] = ((packageCount.shr(8) and (0xff))).toByte()

        bytes[11] = (bytes[4] + bytes[5] + bytes[6] + bytes[7] + bytes[8]
                    + bytes[9] + bytes[10]).toByte()
        return bytes
    }

    fun exSendBigData(cmdType:Int,data:ByteArray,index: Int):ByteArray{
        val size = data.size
        val bytes = createBaseCmdByte(size+7, cmdType, BLE_CTRL_WRITE, size+2)

        bytes[4] = ((index and (0xff))).toByte()
        bytes[5] = ((index.shr(8) and (0xff))).toByte()

        System.arraycopy(data, 0, bytes, 6, size)

        var checkData=0
        for (index in bytes.indices) {
            if (index >= 4)
                checkData += bytes[index].toInt()
        }
        bytes[bytes.size-1]=checkData.toByte()
        return bytes
    }
}