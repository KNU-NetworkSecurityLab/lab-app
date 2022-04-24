package com.example.nsl_app.Utils.notionAPI.DatabaseQuery

import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.CreatedBy
import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.LastEditedBy
import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.Parent
import com.example.nsl_app.Utils.notionAPI.DatabaseQuery.Properties

data class Result(
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
    val url: String
)