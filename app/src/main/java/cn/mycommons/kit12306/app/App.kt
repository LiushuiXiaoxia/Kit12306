package cn.mycommons.kit12306.app

import android.app.Application
import cn.mycommons.kit12306.model.TicketInfo
import cn.mycommons.kit12306.ui.Navigator
import cn.mycommons.kit12306.util.logInfo
import cn.mycommons.kit12306.util.showToast
import com.google.gson.Gson

fun getApp(): App = App.app

class App : Application() {

    companion object {
        lateinit var app: App
    }

    /**
     * 无障碍服务是否开启
     */
    var accessibilityServiceStart = false

    var ticketInfo: TicketInfo? = null

    override fun onCreate() {
        super.onCreate()

        app = this
        registerActivityLifecycleCallbacks(MyActivityLifecycleCallbacks())
    }

    fun onServiceOpen() {
        accessibilityServiceStart = true

        Navigator.gotMain(getApp())
    }

    fun onServiceClose() {
        accessibilityServiceStart = true

        kotlin.runCatching {
            Navigator.goto12306(getApp())
        }.onFailure {
            showToast("12306 启动失败")
        }
    }

    fun onCatchTicketInfo(info: TicketInfo) {
        showToast("获取数据成功")
        ticketInfo = info

        logInfo(Gson().toJson(info))

        Navigator.gotMain(this)
    }
}