package cn.mycommons.kit12306.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import cn.mycommons.kit12306.app.getApp
import cn.mycommons.kit12306.model.TicketInfo
import cn.mycommons.kit12306.util.logInfo

class MyService : Service() {

    companion object {
        private const val EXTRA_TICK_INFO = "tick_info"

        fun startService(info: TicketInfo) {
            val intent = Intent(getApp(), MyService::class.java)
            intent.putExtra(EXTRA_TICK_INFO, info)
            getApp().startService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val info = intent?.getSerializableExtra(EXTRA_TICK_INFO) as TicketInfo?
        logInfo("info = $info")

        if (info != null && info.isOk()) {
            getApp().onCatchTicketInfo(info)
        }
        return super.onStartCommand(intent, flags, startId)
    }
}