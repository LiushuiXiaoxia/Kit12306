package cn.mycommons.kit12306.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.mycommons.kit12306.BuildConfig
import cn.mycommons.kit12306.R
import cn.mycommons.kit12306.app.getApp
import cn.mycommons.kit12306.domain.CalendarKit
import cn.mycommons.kit12306.model.TicketInfo
import cn.mycommons.kit12306.util.showToast
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {

    lateinit var btnCheckService: Button
    lateinit var btnOpen12306: Button
    lateinit var btnAddCalendar: Button
    lateinit var tvTickInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCheckService = findViewById(R.id.btnCheckService)
        btnOpen12306 = findViewById(R.id.btnOpen12306)
        btnAddCalendar = findViewById(R.id.btnAddCalendar)
        tvTickInfo = findViewById(R.id.tvTickInfo)

        btnCheckService.setOnClickListener { Navigator.openAccessibilitySetting(this) }
        btnOpen12306.setOnClickListener {
            kotlin.runCatching {
                Navigator.goto12306(getApp())
            }.onFailure {
                if (it is RuntimeException) {
                    showToast(it.message ?: "12306 启动失败")
                } else {
                    showToast("12306 启动失败")
                }
            }
        }
        btnAddCalendar.setOnClickListener {
            if (getApp().ticketInfo != null) {
                CalendarKit.add(getApp().ticketInfo!!)
            }
        }

        // mockData()
    }

    private fun mockData() {
        if (BuildConfig.DEBUG) {
            val text = BufferedReader(InputStreamReader(assets.open("mock.json"))).readText()
            val info = Gson().fromJson(text, TicketInfo::class.java)
            getApp().ticketInfo = info
        }
    }

    override fun onResume() {
        super.onResume()

        btnCheckService.isEnabled = !getApp().accessibilityServiceStart
        btnAddCalendar.isEnabled = getApp().ticketInfo != null

        if (getApp().ticketInfo != null) {
            tvTickInfo.text = "${getApp().ticketInfo}"
        } else {
            tvTickInfo.text = null
        }
    }
}