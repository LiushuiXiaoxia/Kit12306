package cn.mycommons.kit12306.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import cn.mycommons.kit12306.util.logDebug

class MyActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        logDebug("$activity ... onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        logDebug("$activity ... onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        logDebug("$activity ... onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        logDebug("$activity ... onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        logDebug("$activity ... onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        logDebug("$activity ... onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        logDebug("$activity ... onActivityDestroyed")
    }
}