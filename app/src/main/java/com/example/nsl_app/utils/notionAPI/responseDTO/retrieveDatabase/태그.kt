package com.example.nsl_app.utils.notionAPI.responseDTO.retrieveDatabase

data class 태그(
    val id: String,
    val multi_select: MultiSelect,
    val name: String,
    val type: String
)