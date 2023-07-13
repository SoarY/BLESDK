package com.soar.libraryble.constant

const val BLE_CTRL_READ = 0X01 //(READ, 手机向手表发起读数据指令)，
const val BLE_CTRL_WRITE = 0X02 //(WRITE, 手机向手表发送设置数据)，手表主动请求
const val BLE_CTRL_SYNC = 0X03 //(SYNC, 手表向手机数据)
const val BLE_CTRL_RESPONSE = 0X04 //(RESPONSE, 有些功能需要)