package lab.nsl.nsl_app.utils.nslAPI

import lab.nsl.nsl_app.models.BookDetailItem
import lab.nsl.nsl_app.models.BookItemList
import lab.nsl.nsl_app.models.UserInfo
import lab.nsl.nsl_app.models.*
import lab.nsl.nsl_app.models.iot.DoorState
import lab.nsl.nsl_app.models.iot.NslLightState
import lab.nsl.nsl_app.models.iot.TemperatureHumidity
import lab.nsl.nsl_app.utils.SecretConstants
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NSLAPI {

    companion object {
        private const val baseUrl = SecretConstants.NSL_URL

        fun create(): NSLAPI {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NSLAPI::class.java)
        }
    }

    /*
        User
     */

    // 로그인
    @POST("/api/v1/users/sign-in")
    fun loginCall(@Body body: LoginRequestDTO): Call<ResponseBody>

    // 회원가입
    @POST("/api/v1/users/sign-up")
    fun signUpCall(@Body body: SignUpRequestDTO): Call<ResponseBody>

    // 회원정보 받기
    @GET("/api/v1/users")
    fun getUserInfoCall(@Header("Authorization") token: String): Call<UserInfo>


    /*
        Book
     */

    @Multipart
    @POST("/api/v1/books")
    @JvmSuppressWildcards
    fun bookRegisterCall(
        @Header("Authorization") token: String,
        @Part("book") book: BookRequestDTO,
        @Part bookImages: List<MultipartBody.Part>
    ): Call<ResponseBody>

    @GET("/api/v1/books")
    fun getBookListCall(@Header("Authorization") token: String): Call<BookItemList>

    @GET("/api/v1/books/search")
    fun getBookSearchListCall(
        @Header("Authorization") token: String,
        @Query("keyword") keyword: String
    ): Call<BookItemList>

    @GET("/api/v1/books/{bookId}")
    fun getBookDetailCall(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): Call<BookDetailItem>

    @GET("/api/v1/books/images/{imageId}")
    fun getBookImageCall(
        @Header("Authorization") token: String,
        @Path("imageId") imageId: Int
    ): Call<ResponseBody>


    @DELETE("/api/v1/books/{bookId}")
    fun deleteBookCall(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Int
    ): Call<ResponseBody>


    /*
        Github
     */

    @GET("/api/v1/github/organization/languages")
    fun getOrganizationLanguagesCall(
        @Header("Authorization") token: String
    ): Call<LanguagesListModel>


    /*
        IoT
     */

    @GET("/api/v1/iot/door")
    fun getDoorStateCall(
        @Header("Authorization") token: String
    ): Call<DoorState>

    @GET("/api/v1/iot/light")
    fun getLightStateCall(
        @Header("Authorization") token: String
    ): Call<NslLightState>

    @GET("/api/v1/iot/temperature-humidity")
    fun getTemperatureHumidityCall(
        @Header("Authorization") token: String
    ): Call<TemperatureHumidity>
}