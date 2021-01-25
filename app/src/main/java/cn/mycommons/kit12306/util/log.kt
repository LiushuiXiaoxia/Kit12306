package cn.mycommons.kit12306.util

import android.util.Log


fun Any.logDebug(msg: String) {
    val tag = this.javaClass.simpleName
    Log.d(tag, msg)
}

fun Any.logInfo(msg: String) {
    val tag = this.javaClass.simpleName
    Log.i(tag, msg)
}