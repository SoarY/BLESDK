package com.soar.libraryble.constant

/**
 * @ClassName CmdConst
 * @Author  Darcy
 * @Date 2022-3-16 12:09:06
 * @Version 1.0
 */

/**
 * /*找手表*/
 */
const val EVT_TYPE_ALERT_FIND_WATCH = 0

/**
 * /*消息推送*/
 */
const val EVT_TYPE_ALERT_MSG = 11

/**
 * 心电测量
 */
const val EVT_TYPE_ECG_HR = 29

const val EVT_TYPE_MES_HR                = 30/* 心率测量 */
const val EVT_TYPE_MES_BP                = 31/* 血压测量 */
const val EVT_TYPE_MES_BO2               = 32/* 血氧测量 */

const val EVT_TYPE_SHUTDOWN              = 33/* 关机 */
const val EVT_TYPE_REBOOT                = 34/* 重启 */
const val EVT_TYPE_RESET                 = 35/* 恢复出厂 */
const val EVT_TYPE_LOW_POWER_SHUTDOWN    = 36/* 低电关机 */

const val EVT_TYPE_PRODUCT_ID = 60/* 产品ID */
/**
 * TP版本
 */
const val EVT_TYPE_TP_VER = 61


/**
 * 固件版本
 */
const val EVT_TYPE_FIRMWARE_VER = 62


/**
 * UI版本
 */
const val EVT_TYPE_UI_VER = 63


/**
 * GSENSOR坐标
 */
const val EVT_TYPE_GSENSOR_XYZ = 64


/**
 * 用户信息
 */
const val EVT_TYPE_USER_INFO = 65


/**
 * 单位制度
 */
const val EVT_TYPE_UNIT_SYSTEM = 66


/**
 * 日期、时间
 */
const val EVT_TYPE_DATE_TIME = 67

/**
 * 时间格式
 */
const val EVT_TYPE_TIME_MODE = 68

/**
 * 温度格式
 */
const val EVT_TYPE_TEMP_UNIT = 69


/**
 * 语言
 */
const val EVT_TYPE_LANGUAGE = 70

/**
 * 表盘ID
 */
const val EVT_TYPE_TIME_STYLE = 71


/**
 * 电量
 */
const val EVT_TYPE_BAT = 72


/**
 * 目标步数
 */
const val EVT_TYPE_TARGET_STEP = 73


/**
 * 分时计步
 */
const val EVT_TYPE_HOUR_STEP = 74

/**
 * 历史七天运动数据（24小时分时计步、全天运动数据）
 */
const val EVT_TYPE_HISTORY_SPORT_DATA = 75


/**
 * 消息通知
 */
const val EVT_TYPE_NOTIFICATIONS = 76

/**
 * 亮屏时长
 */
const val EVT_TYPE_DISPLAY_TIME = 77


/**
 * 手环配置
 */
const val EVT_TYPE_BAND_CONFIG = 78


/**
 * 睡眠数据
 */
const val EVT_TYPE_SLEEP_DATA = 79

/**
 * 运动记录
 */
const val EVT_TYPE_SPORT_RECORD = 80


/**
 * 运动数据
 */
const val EVT_TYPE_SPORT_DATA = 81

/**
 * 天气预报
 */
const val EVT_TYPE_WEATHER_FORECAST = 82

/**
 * 实时天气
 */
const val EVT_TYPE_REAL_TIME_WEATHER = 83

/**
 * 抬腕亮屏
 */
const val EVT_TYPE_RAISE_WRIST = 84

/**
 * 勿扰模式
 */
const val EVT_TYPE_DISTURB = 85


/**
 * 久坐提醒
 */
const val EVT_TYPE_LONG_SIT = 86


/**
 * 喝水提醒
 */
const val EVT_TYPE_DRINK_WATER = 87


/**
 * 洗手提醒
 */
const val EVT_TYPE_WASH_HAND = 88

/**
 * 日程提醒
 */
const val EVT_TYPE_SCHEDULE = 89

/**
 * 闹钟
 */
const val EVT_TYPE_ALARM = 90

/**
 * 体温
 */
const val EVT_TYPE_TEMP = 91

/**
 * 心率
 */
const val EVT_TYPE_HR = 92

/**
 * 血压
 */
const val EVT_TYPE_BP = 93

/**
 * 血氧
 */
const val EVT_TYPE_BO = 94

/**
 * 全天心率
 */
const val EVT_TYPE_HR_DAY = 95

/**
 * 全天血氧
 */
const val EVT_TYPE_BO_DAY = 96

/**
 * 全天体温
 */
const val EVT_TYPE_TEMP_DAY = 97

/**
 * 全天测量开关
 */
const val EVT_TYPE_ALL_DAY_FALG = 98

/**
 *  测试设置hr/bp/bo2
 */
const val EVT_TYPE_HR_BP_BO2 = 99

/**
 * 拍照
 */
const val EVT_TYPE_TAKE_PHOTO = 100


/**
 * 音乐控制
 */
const val EVT_TYPE_CTRL_MUSIC = 101

/**
 * 手环配置
 */
const val EVT_TYPE_BAND_CONFIG1 = 102

/**
 * 设置手机系统类型
 */
const val EVT_TYPE_PHONE_SYSTEM_TYPE = 103


/**
 * APP请求同步基础数据同步
 */
const val EVT_TYPE_APP_REQUEST_SYNC = 105

/**
 * 查找手机
 */
const val EVT_TYPE_FIND_PHONE = 106


/**
 * 全天血压
 */
const val EVT_TYPE_BP_DAY = 107

/**
 * /*名片/交友码*/
 */
const val EVT_TYPE_ADD_FRIEND = 108

/**
 * 收款码
 */
const val EVT_TYPE_RECEIPT_CODE = 109


/**
 * 女性健康
 */
const val EVT_TYPE_WOMEN_HEALTH = 110


/**
 * 手机接听/挂断状态
 */
const val EVT_TYPE_ANDROID_PHONE_CTRL = 111

/**
 * 解除绑定
 */
const val EVT_TYPE_UNBIND = 112

/**
 * 心电指令
 */
const val EVT_TYPE_HR_ECG = 120

const val EVT_TYPE_DEVICE_FUN    = 121 /*同步固件端支持功能+设备屏幕信息*/


const val EVT_TYPE_WATCH_CALL_INFO  = 122 /* 同步3.0 设备 MAC + 名称*/

/**
 * OTA 开始指令
 */
const val EVT_TYPE_OTA_START = 200

/**
 * OTA 数据指令
 */
const val EVT_TYPE_OTA_DATA = 201

/**
 * OTA 结束
 */
const val EVT_TYPE_OTA_END = 202



