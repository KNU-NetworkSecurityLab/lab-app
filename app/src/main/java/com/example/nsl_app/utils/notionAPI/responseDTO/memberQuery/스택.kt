package com.example.nsl_app.utils.notionAPI.responseDTO.memberQuery

data class 스택(
    val id: String,
    val multi_select: List<MultiSelect>,
    val type: String
)