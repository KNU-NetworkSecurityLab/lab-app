package com.example.nsl_app.utils.notionAPI

import com.example.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Children
import com.example.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Parent
import com.example.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Properties

data class NotionCreateScheduleData(
    val parent: Parent,
    val properties: Properties
)

data class NotionCreateScheduleWithContentData(
    val parent: Parent,
    val properties: Properties,
    val children: List<Children>
)