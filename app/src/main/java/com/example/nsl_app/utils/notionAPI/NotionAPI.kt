package com.example.nsl_app.utils.notionAPI

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NotionAPI {

    companion object {
        private const val baseNotionUri = "https://api.notion.com/"
        const val notionVersion = "2022-02-22"
        const val NOTION_DB_SCHEDULE_ID = "9ef40c5d64c84229b876b801b1c19a15"

        fun create(): NotionAPI {
            return Retrofit.Builder()
                .baseUrl(baseNotionUri)
                .addConverterFactory(GsonConverterFactory.create()).build().create(NotionAPI::class.java)
        }
    }

    @POST("v1/databases/{DATABASE_ID}/query")
    fun queryNotionDataBaseAll(
        @Path("DATABASE_ID") databaseId: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token:String
    ): Call<NotionDatabaseQueryResponse>


    @GET("v1/databases/{DATABASE_ID}")
    fun getNotionRetrieveData(
        @Path("DATABASE_ID") databaseId: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token:String
    ): Call<NotionRetrieveDatabaseResponse>


    @POST("v1/pages")
    fun registerSchedule(
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token:String,
        @Body notionCreateScheduleData: NotionCreateScheduleData
    ): Call<NotionScheduleCreateResponse>

    @POST("v1/pages")
    fun registerScheduleWithContent(
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token:String,
        @Body notionCreateScheduleWithContentData: NotionCreateScheduleWithContentData
    ): Call<NotionScheduleCreateResponse>
}