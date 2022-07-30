package com.example.nsl_app.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*

object Utils {
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

    fun getDisplayWidthHeight(activity: Activity): Array<Int> {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        return arrayOf(width, height)
    }
}