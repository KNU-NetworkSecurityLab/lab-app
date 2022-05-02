package com.example.nsl_app.utils.notionAPI.responseDTO.databaseQuery

data class cal_tags(
    val id: String,
    val multi_select: List<MultiSelect>,
    val type: String
)