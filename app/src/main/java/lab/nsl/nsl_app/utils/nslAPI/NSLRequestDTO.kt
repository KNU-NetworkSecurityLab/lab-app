package lab.nsl.nsl_app.utils.nslAPI

import com.google.gson.annotations.SerializedName

data class LoginRequestDTO(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("password") val password: String
)

data class SignUpRequestDTO(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
)

data class BookRequestDTO(
    val bookName: String,
    val bookAuthor: String,
    val bookPublisher: String,
    val bookTagList: ArrayList<String>
)