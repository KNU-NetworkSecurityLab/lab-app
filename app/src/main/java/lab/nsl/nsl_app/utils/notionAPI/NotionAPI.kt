package lab.nsl.nsl_app.utils.notionAPI

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NotionAPI {

    companion object {
        private const val baseNotionUri = "https://api.notion.com/"
        const val NOTION_API_VERSION = "2022-06-28"
        const val NOTION_SCHEDULE_DB_ID = "30ac3457537744b69abb66cbe6be134a"
        const val NOTION_MEMBER_DB_ID = "5d519222a4ea4084be38df311b236487"

        fun create(): NotionAPI {
            return Retrofit.Builder()
                .baseUrl(baseNotionUri)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(NotionAPI::class.java)
        }
    }

    @POST("v1/databases/{DATABASE_ID}/query")
    fun queryNotionDataBaseAll(
        @Path("DATABASE_ID") databaseId: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String
    ): Call<NotionDatabaseQueryResponse>


    @GET("v1/databases/{DATABASE_ID}")
    fun getNotionRetrieveData(
        @Path("DATABASE_ID") databaseId: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String
    ): Call<NotionRetrieveDatabaseResponse>

    // 페이지 등록
    @POST("v1/pages")
    fun registerSchedule(
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
        @Body notionCreateScheduleData: NotionCreateScheduleData
    ): Call<NotionScheduleCreateResponse>

    // 내용과 함께 페이지 등록
    @POST("v1/pages")
    fun registerScheduleWithContent(
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
        @Body notionCreateScheduleWithContentData: NotionCreateScheduleWithContentData
    ): Call<NotionScheduleCreateResponse>

    // 일정 정보 받아오기
    @GET("v1/pages/{id}")
    fun getScheduleInfo(
        @Path("id") pageID: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
    ): Call<NotionScheduleCreateResponse>

    // 페이지 수정
    @PATCH("v1/pages/{id}")
    fun editSchedule(
        @Path("id") pageID: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
        @Body notionEditSchedule: NotionEditSchedule
    ): Call<ResponseBody>

    // 페이지 삭제
    @DELETE("v1/blocks/{id}")
    fun deleteSchedule(
        @Path("id") pageID: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String,
    ): Call<ResponseBody>

    // 구성원 받아오기
    @POST("v1/databases/{DATABASE_ID}/query")
    fun getMember(
        @Path("DATABASE_ID") databaseID: String,
        @Header("Notion-Version") notionVersion: String,
        @Header("Authorization") token: String
    ): Call<NotionMemberResponse>
}