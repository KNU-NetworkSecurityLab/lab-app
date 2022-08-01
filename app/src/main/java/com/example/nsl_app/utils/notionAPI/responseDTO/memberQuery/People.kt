package com.example.nsl_app.utils.notionAPI.responseDTO.memberQuery

data class People(
    val avatar_url: String,
    val id: String,
    val name: String,
    val `object`: String,
    val person: Person,
    val type: String
)