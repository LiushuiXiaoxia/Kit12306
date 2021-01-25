package cn.mycommons.kit12306.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import cn.mycommons.kit12306.app.getApp
import cn.mycommons.kit12306.model.TicketInfo
import cn.mycommons.kit12306.util.logInfo

class MyAccessibilityService : AccessibilityService() {

    companion object {
        private const val CLASS_NAME = "com.alipay.mobile.nebulacore.ui.H5Activity"
    }

    override fun onServiceConnected() {
        logInfo("onServiceConnected")

        getApp().onServiceOpen()
    }

    override fun onDestroy() {
        logInfo("onDestroy")

        getApp().onServiceClose()
    }

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        logInfo("eventType ${event.eventType} packageName ${event.packageName} className ${event.className}")

        if (( // event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            && rootInActiveWindow != null
            && CLASS_NAME == event.className
        ) {
            val webViewRoot = rootInActiveWindow?.let { findWebViewRoot(it) }
            logInfo("webViewRoot = $webViewRoot")

            if (webViewRoot != null) {
                runCatching { doSearch(webViewRoot) }
            }
        }
    }

    private fun findWebViewRoot(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (node.className == "android.webkit.WebView" && node.text == "订单详情") {
            return node
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                val ret = findWebViewRoot(child)
                if (ret != null) {
                    return ret
                }
            }
        }
        return null
    }

    private fun doSearch(webViewRoot: AccessibilityNodeInfo) {
        val orderId = webViewRoot.getChild(0)?.getChild(0)?.getChild(0)?.getChild(1)
        logInfo("orderId = ${orderId?.text}")
        val child = webViewRoot.getChild(0)?.getChild(0)?.getChild(0)?.getChild(3)
        val child2 = webViewRoot.getChild(0)?.getChild(0)?.getChild(0)?.getChild(5)

        if (child == null || child2 == null) {
            return
        }

        val fromStation = child.getChild(0)
        val fromTime = child.getChild(1)
        val trainNo = child.getChild(2)
        val toStation = child.getChild(6)
        val toTime = child.getChild(7)
        val fromDate = child.getChild(8)
        val fromPort = child.getChild(9)

        logInfo("fromStation = ${fromStation?.text}")
        logInfo("fromStation = ${fromTime?.text}")
        logInfo("fromStation = ${trainNo?.text}")
        logInfo("fromStation = ${toStation?.text}")
        logInfo("fromStation = ${toTime?.text}")
        logInfo("toTime2 = ${fromDate?.text}")
        logInfo("toTime2 = ${fromPort?.text}")

        val personName = child2.getChild(0)?.getChild(0)
        val price = child2.getChild(1)
        val seatLevel = child2.getChild(2)
        val seatNo = child2.getChild(3)
        val seatNo2 = child2.getChild(4)

        logInfo("personName = ${personName?.text}")
        logInfo("personName = ${price?.text}")
        logInfo("personName = ${seatLevel?.text}")
        logInfo("personName = ${seatNo?.text}")
        logInfo("personName = ${seatNo2?.text}")

        val info = TicketInfo().also {
            it.trainNo = trainNo?.text?.toString()
            it.fromStation = fromStation?.text?.toString()
            it.fromTime = fromTime?.text?.toString()
            it.toStation = toStation?.text?.toString()
            it.toTime = toTime?.text?.toString()
            it.fromDate = fromDate?.text?.toString()
            it.fromPort = fromPort?.text?.toString()

            it.personName = personName?.text?.toString()
            it.ticketPrice = price?.text?.toString()
            it.seatLevel = seatLevel?.text?.toString()
            it.seatNo = seatNo?.text?.toString()
            it.seatNo2 = seatNo2?.text?.toString()
        }

        logInfo("info = $info")

        if (info.isOk()) {
            MyService.startService(info)
        }
    }
}