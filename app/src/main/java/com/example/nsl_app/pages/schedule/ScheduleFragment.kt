package com.example.nsl_app.pages.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.EventDay
import com.example.nsl_app.R
import com.example.nsl_app.databinding.FragmentScheduleBinding
import com.example.nsl_app.pages.schedule.scheduleAdd.ScheduleAddActivity
import com.example.nsl_app.utils.notionAPI.NotionAPI
import com.example.nsl_app.utils.notionAPI.NotionDatabaseQueryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ScheduleFragment : Fragment() {

    data class ScheduleData (val title: String, var startDate: Date?, var endDate: Date?)

    private lateinit var binding: FragmentScheduleBinding
    private val notionAPI by lazy { NotionAPI.create() }
    private val scheduleDataList = ArrayList<ScheduleData>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        binding.run {
            btnCalAdd.setOnClickListener {
                val intent = Intent(requireContext(), ScheduleAddActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getSchedules()
    }


    private fun addEvent() {
        val events: MutableList<EventDay> = ArrayList()

        scheduleDataList.forEach { it ->

            if(it.endDate == null) {
                // 종료일이 없을 경우

                val tempCal = Calendar.getInstance().apply { timeInMillis = it.startDate!!.time }
                events.add(EventDay(tempCal, R.drawable.circle_main_10))

            } else {
                // 종료일이 있을 경우

                val startCal = Calendar.getInstance().apply {
                    timeInMillis = it.startDate!!.time
                    set(Calendar.HOUR, 3)
                }
                val endCal = Calendar.getInstance().apply {
                    timeInMillis = it.endDate!!.time
                    set(Calendar.HOUR, 5)
                }

                while(startCal.timeInMillis < endCal.timeInMillis) {
                    val tempCal = Calendar.getInstance().apply { timeInMillis = startCal.timeInMillis }

                    events.add(EventDay(tempCal, R.drawable.circle_main_10))
                    startCal.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }

        binding.cvLabSchedule.setEvents(events)

//        events.add(EventDay(c1, R.drawable.circle_main, Color.BLUE))
//        events.add(EventDay(c2_1, R.drawable.circle_main_10, Color.RED))
//        events.add(EventDay(c2, CalendarUtils.getDrawableText(requireContext(), "Text",null, android.R.color.black, 8)))

    }

    private fun getSchedules () {
        val call = notionAPI.queryNotionDataBaseAll(NotionAPI.NOTION_DB_SCHEDULE_ID, NotionAPI.notionVersion, getString(R.string.secret_notion_key))

        call.enqueue(object : Callback<NotionDatabaseQueryResponse> {
            override fun onResponse(
                call: Call<NotionDatabaseQueryResponse>,
                response: Response<NotionDatabaseQueryResponse>
            ) {
                if (response.isSuccessful) {
                    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val body = response.body() as NotionDatabaseQueryResponse

                    body.results.forEach {
                        if (it.properties.이름.title.isNotEmpty()) {
                            val text = it.properties.이름.title[0].text.content
                            val dateStart = it.properties.날짜.date.start
                            val dateEnd = it.properties.날짜.date.end

                            Log.d("responseDevvv","${text}   $dateStart     $dateEnd")

                            val tempScheduleData = ScheduleData(text, null, null)

                            if(dateStart.trim().length == 10) {
                                // 2022-05-06 (시간 미포함)
                                tempScheduleData.startDate = dateFormat.parse(dateStart)
                            } else {
                                // 2022-05-06T20:00:00.000+09:00 (시간 포함)
                                tempScheduleData.startDate = dateTimeFormat.parse(dateStart)
                            }

                            if(dateEnd != null) {
                                if(dateEnd.trim().length == 10) {
                                    // 2022-05-06 (시간 미포함)
                                    tempScheduleData.endDate = dateFormat.parse(dateEnd)
                                } else {
                                    // 2022-05-06T20:00:00.000+09:00 (시간 포함)
                                    tempScheduleData.endDate = dateTimeFormat.parse(dateEnd)
                                }
                            }

                            scheduleDataList.add(tempScheduleData)

                        }
                    }

                    addEvent()
                }
            }

            override fun onFailure(call: Call<NotionDatabaseQueryResponse>, t: Throwable) {

            }
        })
    }
}