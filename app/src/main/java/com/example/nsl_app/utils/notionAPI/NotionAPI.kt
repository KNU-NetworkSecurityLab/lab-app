package com.example.nsl_app.utils.notionAPI

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface NotionAPI {

    companion object {
        private const val baseNotionUri = "https://api.notion.com/"
        const val notionVersion = "2022-02-22"
        const val NOTION_DB_SCHEDULE_ID = "1f12850f91f74bc8ac6fcc68f289ec09"

        fun create(): NotionAPI {
            return Retrofit.Builder()
                .baseUrl(baseNotionUri)
                .addConverterFactory(GsonConverterFactory.create()).build().create(NotionAPI::class.java)
        }
    }

    @POST("v1/databases/{DATABASE_ID}/query")
    fun notionDataBaseAll(
        @Path("DATABASE_ID") databaseId: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token:String
    ): Call<ResponseNotionDatabaseQuery>


}