package com.soar.libraryble.protocol.cmd

/**
 * NAME：YONG_
 * Created at: 2023/6/19 11
 * Describe:
 */
object CmdAnalysisImpl {

    fun parserFirmware(byteArray: ByteArray):String{
        val resLength = byteArray[3].toInt()
        val resultByte = ByteArray(resLength)
        System.arraycopy(byteArray, 4, resultByte, 0, resLength)
        return String(resultByte)
    }

    fun parserEndBigData(byteArray: ByteArray):Boolean{
        val status = byteArray[4].toInt()
        return status==0
    }

}