package com.tdh.windydemo.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {
    companion object {
        fun getTimeStamp(): String {
            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat.getDateTimeInstance()
            return simpleDateFormat.format(calendar.time)
        }
    }
}