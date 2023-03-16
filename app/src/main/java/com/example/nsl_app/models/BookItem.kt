package com.example.nsl_app.models

data class BookItem(
    val bookId: Int,
    val bookName: String?,
    val bookAuthor: String?,
    val bookTags: ArrayList<String>?
)
