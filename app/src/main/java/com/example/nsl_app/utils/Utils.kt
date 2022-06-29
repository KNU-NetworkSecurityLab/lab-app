package com.example.nsl_app.utils

import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        val notionDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        fun isEqualDate(date1: Long, date2: Long): Boolean {
            val cal1 = Calendar.getInstance().apply { timeInMillis = date1 }
            val cal2 = Calendar.getInstance().apply { timeInMillis = date2 }
            return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                    cal1[Calendar.MONTH] == cal2[Calendar.MONTH] &&
                    cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH]
        }

        fun getBase64Decode(content: String): String {
            return String(Base64.decode(content.replace("\n", ""), Base64.DEFAULT), charset("UTF-8"))
        }
    }
}