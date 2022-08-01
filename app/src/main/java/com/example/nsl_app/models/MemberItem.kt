package com.example.nsl_app.models

data class MemberItem(
    val name: String,
    val studentNumber: String, // 학번
    val position: String, // 직책 - 랩장, 부원 등
    val email: String?,
    val skills: ArrayList<String>
)
