package com.example.nsl_app.utils

import java.util.*

class Utils {
    companion object {
        fun isEqualDate(date1 : Long, date2: Long): Boolean {
            val cal1 = Calendar.getInstance().apply { timeInMillis = date1 }
            val cal2 = Calendar.getInstance().apply { timeInMillis = date2 }
            return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] &&
                    cal1[Calendar.MONTH] == cal2[Calendar.MONTH] &&
                    cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH]
        }
    }
}