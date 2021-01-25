package cn.mycommons.kit12306.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class TicketInfo : Serializable {

    var trainNo: String? = null
    var fromStation: String? = null
    var fromTime: String? = null
    var toStation: String? = null
    var toTime: String? = null
    var fromDate: String? = null
    var fromPort: String? = null // 检票口

    var personName: String? = null
    var ticketPrice: String? = null
    var seatLevel: String? = null
    var seatNo: String? = null // 车号
    var seatNo2: String? = null // 座位号

    fun isOk(): Boolean {
        return trainNo != null && fromStation != null && toStation != null
    }

    fun getFormDateTime(): Date? {
        if (fromDate != null && fromTime != null) {
            val fromDateStr = fromDate!!.replace("发车时间：", "")
            return SimpleDateFormat("yyyy年MM月dd日 EEE HH:mm", Locale.getDefault()).parse("$fromDateStr $fromTime")
        }
        return null
    }

    override fun toString(): String {
        return "TicketInfo(" +
                "trainNo=$trainNo, " +
                "fromStation=$fromStation, " +
                "fromTime=$fromTime, " +
                "toStation=$toStation, " +
                "toTime=$toTime, " +
                "fromDate=$fromDate, " +
                "fromPort=$fromPort, " +
                "personName=$personName, " +
                "ticketPrice=$ticketPrice, " +
                "seatLevel=$seatLevel, " +
                "seatNo=$seatNo, " +
                "seatNo2=$seatNo2" +
                ")"
    }
}