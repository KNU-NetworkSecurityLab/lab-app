package com.example.nsl_app.utils.notionAPI

import com.example.nsl_app.utils.notionAPI.DatabaseQuery.Page
import com.example.nsl_app.utils.notionAPI.DatabaseQuery.Result
import com.example.nsl_app.utils.notionAPI.retrieveDatabase.*

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