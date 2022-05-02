package com.example.nsl_app.utils.notionAPI

import com.example.nsl_app.utils.notionAPI.dataDTO.Parent
import com.example.nsl_app.utils.notionAPI.dataDTO.Properties

data class NotionCreateScheduleData(
    val parent: Parent,
    val properties: Properties
)