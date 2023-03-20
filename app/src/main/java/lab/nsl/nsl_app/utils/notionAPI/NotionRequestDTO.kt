package lab.nsl.nsl_app.utils.notionAPI

import lab.nsl.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Children
import lab.nsl.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Parent
import lab.nsl.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Properties

data class NotionCreateScheduleData(
    val parent: Parent,
    val properties: Properties
)

data class NotionCreateScheduleWithContentData(
    val parent: Parent,
    val properties: Properties,
    val children: List<Children>
)

data class NotionEditSchedule(
    val properties: Properties
)