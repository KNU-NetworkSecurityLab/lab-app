package com.example.nsl_app.utils.nslAPI

import com.example.nsl_app.utils.SecretConstants
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
    @POST("login")
    fun loginCall(@Body body: LoginRequestDTO): Call<ResponseBody>

    // 회원가입
    @POST("api/v1/signup")
    fun signUpCall(@Body body: SignUpRequestDTO): Call<ResponseBody>


}