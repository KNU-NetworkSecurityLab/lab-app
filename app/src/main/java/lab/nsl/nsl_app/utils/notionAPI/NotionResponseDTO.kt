package lab.nsl.nsl_app.utils.notionAPI

import lab.nsl.nsl_app.utils.notionAPI.responseDTO.databaseQuery.Page
import lab.nsl.nsl_app.utils.notionAPI.responseDTO.databaseQuery.Result
import lab.nsl.nsl_app.utils.notionAPI.responseDTO.retrieveDatabase.*

data class NotionDatabaseQueryResponse(
    val has_more: Boolean,
    val next_cursor: Any,
    val `object`: String,
    val page: Page,
    val results: List<Result>,
    val type: String
)


data class NotionRetrieveDatabaseResponse(
    val archived: Boolean,
    val cover: Any,
    val created_by: CreatedBy,
    val created_time: String,
    val icon: Any,
    val id: String,
    val last_edited_by: LastEditedBy,
    val last_edited_time: String,
    val `object`: String,
    val parent: Parent,
    val properties: Properties,
    val title: List<TitleX>,
    val url: String
)

data class NotionScheduleCreateResponse(
    val archived: Boolean,
    val cover: Any,
    val created_by: lab.nsl.nsl_app.utils.notionAPI.responseDTO.scheduleCreate.CreatedBy,
    val created_time: String,
    val icon: Any,
    val id: String,
    val last_edited_by: lab.nsl.nsl_app.utils.notionAPI.responseDTO.scheduleCreate.LastEditedBy,
    val last_edited_time: String,
    val `object`: String,
    val parent: lab.nsl.nsl_app.utils.notionAPI.responseDTO.scheduleCreate.Parent,
    val properties: lab.nsl.nsl_app.utils.notionAPI.responseDTO.scheduleCreate.Properties,
    val url: String
)

data class NotionMemberResponse(
    val has_more: Boolean,
    val next_cursor: Any,
    val `object`: String,
    val page: lab.nsl.nsl_app.utils.notionAPI.responseDTO.memberQuery.Page,
    val results: List<lab.nsl.nsl_app.utils.notionAPI.responseDTO.memberQuery.Result>,
    val type: String
)