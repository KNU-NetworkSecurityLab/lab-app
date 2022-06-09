package com.example.nsl_app.utils.nslAPI

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NslAPI {

    companion object {
        private const val baseUrl = NSLUrl.NSL_URL

        fun create(): NslAPI {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NslAPI::class.java)
        }
    }

    /*
        User
     */

    // 로그인
    @POST("login")
    fun loginCall(@Body body: LoginRequestDTO): Call<ResponseBody>

    // 회원가입
    @POST("api/v1/signup")
    fun signUpCall(@Body body: SignUpRequestDTO): Call<ResponseBody>


}