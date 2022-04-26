package com.example.nsl_app.utils.notionAPI

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface NotionAPI {

    @POST("v1/databases/{DATABASE_ID}/query")
    fun notionDataBaseAll(
        @Path("DATABASE_ID") databaseId: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token:String
    ): Call<ResponseNotionDatabaseQuery>


}