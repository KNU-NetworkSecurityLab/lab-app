package com.example.nsl_app.Utils.notionAPI.DatabaseQuery

import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.cal_date
import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.cal_name
import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.cal_tags

data class Properties(
    val 날짜: cal_date,
    val 이름: cal_name,
    val 태그: cal_tags
)