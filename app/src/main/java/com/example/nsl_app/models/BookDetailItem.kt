package com.example.nsl_app.models

data class BookDetailItem(
    var bookName: String,
    var bookAuthor: String,
    var bookPublisher: String,
    var bookStock: Int,
    var bookTagList: ArrayList<String>,
    var bookImageList: ArrayList<Int>
)