package com.example.nsl_app.utils.notionAPI.responseDTO.retrieveDatabase

data class Annotations(
    val bold: Boolean,
    val code: Boolean,
    val color: String,
    val italic: Boolean,
    val strikethrough: Boolean,
    val underline: Boolean
)