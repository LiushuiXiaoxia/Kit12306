package cn.mycommons.kit12306.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings

object Navigator {

    fun openAccessibilitySetting(context: Context) {
        try {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        } catch (e: Exception) {
            context.startActivity(Intent(Settings.ACTION_SETTINGS))
            e.printStackTrace()
        }
    }

    fun gotMain(context: Context) {
        val main = Intent(context, MainActivity::class.java)
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(main)
    }

    // com.MobileTicket
    fun goto12306(context: Context) {
        val pkg = "com.MobileTicket"
        val intent = context.packageManager.getLaunchIntentForPackage(pkg)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}