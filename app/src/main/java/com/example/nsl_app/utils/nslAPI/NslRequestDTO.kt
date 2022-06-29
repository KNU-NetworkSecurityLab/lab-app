package com.example.nsl_app.utils.nslAPI

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO(
    @SerializedName("userId") val userId: String,
    @SerializedName("password") val password: String
)

data class SignUpRequestDTO(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("mail") val mail: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("position") val position: String,
    @SerializedName("studentId") val studentId: String
)