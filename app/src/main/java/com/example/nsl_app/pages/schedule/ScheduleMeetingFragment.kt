package com.example.nsl_app.pages.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nsl_app.R
import com.example.nsl_app.utils.notionAPI.NotionAPI
import com.example.nsl_app.utils.notionAPI.ResponseNotionDatabaseQuery
import com.example.nsl_app.databinding.FragmentScheduleMeetingBinding
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


class ScheduleMeetingFragment : Fragment() {
    private lateinit var binding: FragmentScheduleMeetingBinding
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleMeetingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        test_getNotionSchdules()
    }


    private fun test_getNotionSchdules() {
        val token = getString(R.string.secret_notion_key)

        val notionAPI = NotionAPI.create()
        val call = notionAPI.notionDataBaseAll(NotionAPI.NOTION_DB_SCHEDULE_ID, NotionAPI.notionVersion, token)

        call.enqueue(object : Callback<ResponseNotionDatabaseQuery> {
            override fun onResponse(
                call: Call<ResponseNotionDatabaseQuery>,
                response: Response<ResponseNotionDatabaseQuery>
            ) {
                if (response.isSuccessful) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val body = response.body() as ResponseNotionDatabaseQuery

                    body.results.forEach {
                        if (it.properties.이름.title.isNotEmpty()) {
                            val text = it.properties.이름.title[0].text.content
                            val date = it.properties.날짜.date.start
                            val tvText = binding.tvNotion.text.toString()
                            binding.tvNotion.text = "$tvText\n$date $text"
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseNotionDatabaseQuery>, t: Throwable) {

            }
        })
    }


}