package com.example.nsl_app.Utils.notionAPI.DatabaseQuery

data class Title(
    val annotations: Annotations,
    val href: Any,
    val plain_text: String,
    val text: Text,
    val type: String
)