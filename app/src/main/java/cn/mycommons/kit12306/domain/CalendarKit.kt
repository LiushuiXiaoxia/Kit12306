package cn.mycommons.kit12306.domain

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL
import cn.mycommons.kit12306.app.getApp
import cn.mycommons.kit12306.model.TicketInfo
import cn.mycommons.kit12306.util.showToast
import java.util.*


object CalendarKit {

    private const val CALENDER_URL = "content://com.android.calendar/calendars"
    private const val CALENDER_EVENT_URL = "content://com.android.calendar/events"
    private const val CALENDER_REMINDER_URL = "content://com.android.calendar/reminders"

    private const val CALENDAR_ACCOUNT_NAME = "kit12306"


    private var contentResolver: ContentResolver = getApp().contentResolver

    private fun isHaveCalender(): Int {
        var ret = -1
        // 查询日历表的cursor
        val cursor: Cursor? = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            cursor.use {
                if (it.moveToFirst()) {
                    val idIdx = cursor.getColumnIndex(CalendarContract.Calendars._ID)
                    ret = cursor.getInt(idIdx)
                }
            }
        }
        return ret
    }

    /**
     * 添加日历表
     */
    private fun addCalendar(): Long {
        val value = ContentValues()
        value.put(CalendarContract.Calendars.NAME, "车票小助手")
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.VISIBLE, 1)
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.RED)
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().id)
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDAR_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        value.put(CalendarContract.CALLER_IS_SYNCADAPTER, 0)
        value.put(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        val insertUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
//            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "false")
//            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
//            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDAR_ACCOUNT_NAME)
            .build()

        val uri: Uri? = contentResolver.insert(insertUri, value)
        return ContentUris.parseId(uri!!)
    }

    fun add(info: TicketInfo) {
        val fromDateTime = info.getFormDateTime()
        if (fromDateTime == null) {
            getApp().showToast("发车时间解析错误")
            return
        }

        // 创建contentResolver
        contentResolver = getApp().contentResolver

        // 日历表id
        var calendarId: Int = isHaveCalender()
        if (calendarId == -1) {
            addCalendar()
            calendarId = isHaveCalender()
        }

        val startMillis = fromDateTime.time
        val endMillis = fromDateTime.time

        // 准备event
        val valueEvent = ContentValues()
        valueEvent.put(CalendarContract.Events.DTSTART, startMillis)
        valueEvent.put(CalendarContract.Events.DTEND, endMillis)
        valueEvent.put(CalendarContract.Events.CALENDAR_ID, calendarId)
        valueEvent.put(CalendarContract.Events.TITLE, "${info.fromStation}/${info.toStation}(${info.trainNo})")
        val s = """发车时间:${info.fromTime}
            |到达时间:${info.toTime}
            |检票口:${info.fromPort}
            |座位号:${info.seatNo}${info.seatNo2}(${info.seatLevel})
        """.trimMargin()
        valueEvent.put(CalendarContract.Events.DESCRIPTION, s)
        valueEvent.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai")

        // 添加event

        // 添加event
        val uri: Uri? = contentResolver.insert(CalendarContract.Events.CONTENT_URI, valueEvent)
        if (uri == null) {
            getApp().showToast("添加event失败")
            return
        }

        // 添加提醒
        val eventId = ContentUris.parseId(uri)
        val valueReminder = ContentValues()
        valueReminder.put(CalendarContract.Reminders.EVENT_ID, eventId)
        valueReminder.put(CalendarContract.Reminders.MINUTES, 15)
        valueReminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM)
        val reminderUri: Uri? = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, valueReminder)
        if (reminderUri == null) {
            getApp().showToast("添加reminder失败")
        }
    }
}