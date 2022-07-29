package com.example.nsl_app.utils.githubAPI

import com.example.nsl_app.utils.githubAPI.responseDTO.ReadMeDTO
import com.example.nsl_app.utils.githubAPI.responseDTO.RepoListDTO
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubAPI {
    companion object {
        private const val baseUrl = "https://api.github.com/"
        const val githubAccountName = "KNU-NetworkSecurityLab"

        fun create(): GithubAPI {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubAPI::class.java)
        }
    }

    // 저장소 리스트 가져오기
    @GET("users/{AccountName}/repos")
    fun getRepos(@Path("AccountName") accountName: String): Call<RepoListDTO>

    @GET("repos/{AccountName}/{RepoName}/readme")
    fun getReadMe(@Path("AccountName") accountName: String, @Path("RepoName") repoName:String): Call<ReadMeDTO>

}