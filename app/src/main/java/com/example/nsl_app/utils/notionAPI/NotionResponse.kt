package com.example.nsl_app.utils.notionAPI

import com.example.nsl_app.utils.notionAPI.DatabaseQuery.Page
import com.example.nsl_app.utils.notionAPI.DatabaseQuery.Result

data class ResponseNotionDatabaseQuery(
    val has_more: Boolean,
    val next_cursor: Any,
    val `object`: String,
    val page: Page,
    val results: List<Result>,
    val type: String
)