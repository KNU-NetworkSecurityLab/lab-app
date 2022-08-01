package com.example.nsl_app.utils.notionAPI.responseDTO.memberQuery

data class Result(
    val archived: Boolean,
    val cover: Any,
    val created_by: CreatedBy,
    val created_time: String,
    val icon: Icon,
    val id: String,
    val last_edited_by: LastEditedBy,
    val last_edited_time: String,
    val `object`: String,
    val parent: Parent,
    val properties: Properties,
    val url: String
)