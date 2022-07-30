package com.example.nsl_app.pages.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.example.nsl_app.R
import com.example.nsl_app.databinding.FragmentScheduleBinding
import com.example.nsl_app.adapters.ScheduleAdapter
import com.example.nsl_app.utils.Constants
import com.example.nsl_app.utils.SecretConstants
import com.example.nsl_app.utils.Utils
import com.example.nsl_app.utils.notionAPI.NotionAPI
import com.example.nsl_app.utils.notionAPI.NotionDatabaseQueryResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ScheduleFragment : Fragment() {

    data class ScheduleData (
        val id: String,
        val title: String,
        var startDate: Date?,
        var startIsIncludeTime: Boolean,
        var endDate: Date?,
        var endIsIncludeTime: Boolean)

    private lateinit var binding: FragmentScheduleBinding
    private val notionAPI by lazy { NotionAPI.create() }
    private val scheduleDataList = ArrayList<ScheduleData>()
    private val targetEventList = ArrayList<ScheduleData>()
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val events: MutableList<EventDay> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)

        scheduleAdapter = ScheduleAdapter(requireContext(), targetEventList)

        binding.run {
            btnCalAdd.setOnClickListener {
                val intent = Intent(requireContext(), ScheduleAddActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_WRITE_OR_EDIT_MODE, Constants.INTENT_EXTRA_WRITE_MODE)
                startActivity(intent)
            }

            cvLabSchedule.setOnDayClickListener { selectedDay ->
                targetEventList.clear()


                // 데이터 필터링

                // 1일 일정 (끝나는 날이 없을때) -> 같은날인것만 필터링
                val filteredData1 = scheduleDataList.filter { scheduleData ->
                    (scheduleData.endDate == null) && Utils.isEqualDate(scheduleData.startDate!!.time, selectedDay.calendar.timeInMillis)
                }

                val filteredData2 = scheduleDataList.filter { scheduleData ->
                    if(scheduleData.endDate != null) {
                        val startDate = Calendar.getInstance().apply {
                            timeInMillis = scheduleData.startDate!!.time
                            set(Calendar.HOUR, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val endDate = Calendar.getInstance().apply {
                            timeInMillis = scheduleData.endDate!!.time
                            set(Calendar.HOUR, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
                        }
                        startDate.timeInMillis <= selectedDay.calendar.timeInMillis && selectedDay.calendar.timeInMillis <= endDate.timeInMillis
                    } else false
                }
                // 2일 이상 일정
                targetEventList.addAll(filteredData1)
                targetEventList.addAll(filteredData2)

                scheduleAdapter.notifyDataSetChanged()
            }

            // 일정 아이템의 More 버튼 클릭 시 이벤트 처리
            scheduleAdapter.scheduleListener = object : ScheduleAdapter.ScheduleListener {
                override fun onMoreClick(v: View, scheduleData: ScheduleData) {
                    val morePopup = PopupMenu(requireContext(), v)
                    requireActivity().menuInflater.inflate(R.menu.schedule_menu, morePopup.menu)
                    morePopup.setOnMenuItemClickListener { menuItem ->
                        when(menuItem.itemId) {
                            R.id.menu_sch_edit -> {
                                val intent = Intent(requireContext(), ScheduleAddActivity::class.java)
                                intent.putExtra(Constants.INTENT_EXTRA_WRITE_OR_EDIT_MODE, Constants.INTENT_EXTRA_EDIT_MODE)
                                intent.putExtra(Constants.INTENT_EXTRA_PAGE_ID, scheduleData.id)
                                startActivity(intent)
                            }
                            R.id.menu_sch_delete -> {
                                val call = notionAPI.deleteSchedule(scheduleData.id, NotionAPI.NOTION_API_VERSION, SecretConstants.SECRET_NOTION_TOKEN)
                                call.enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(
                                        call: Call<ResponseBody>,
                                        response: Response<ResponseBody>
                                    ) {
                                        if(response.isSuccessful) {
                                            Toast.makeText(context, getString(R.string.msg_sch_delete_complete),Toast.LENGTH_SHORT).show()
                                            scheduleDataList.removeIf { it.id == scheduleData.id }
                                            scheduleAdapter.scheduleItemList.removeIf { it.id == scheduleData.id}
                                            scheduleAdapter.notifyDataSetChanged()
                                            addEventOnCalendar()
                                            
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
                                })
                            }
                        }
                        false
                    }

                    morePopup.show()
                }
            }

            listSchedule.adapter = scheduleAdapter
            listSchedule.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            scheduleAdapter.notifyDataSetChanged()

            cvLabSchedule.setDate(Calendar.getInstance())

        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getSchedules()
    }


    private fun addEventOnCalendar() {
        events.clear()

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
        val call = notionAPI.queryNotionDataBaseAll(NotionAPI.NOTION_SCHEDULE_DB_ID, NotionAPI.NOTION_API_VERSION, SecretConstants.SECRET_NOTION_TOKEN)

        call.enqueue(object : Callback<NotionDatabaseQueryResponse> {
            override fun onResponse(
                call: Call<NotionDatabaseQueryResponse>,
                response: Response<NotionDatabaseQueryResponse>
            ) {
                if (response.isSuccessful) {
                    scheduleDataList.clear()

                    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val body = response.body() as NotionDatabaseQueryResponse

                    body.results.forEach {
                        if (it.properties.이름.title.isNotEmpty()) {
                            val id = it.id
                            val text = it.properties.이름.title[0].text.content
                            val dateStart = it.properties.날짜.date.start
                            val dateEnd = it.properties.날짜.date.end

                            val tempScheduleData = ScheduleData(id ,text, null, false,null, false)

                            if(dateStart.trim().length == 10) {
                                // 2022-05-06 (시간 미포함)
                                tempScheduleData.startDate = dateFormat.parse(dateStart)
                                tempScheduleData.startIsIncludeTime = false
                            } else {
                                // 2022-05-06T20:00:00.000+09:00 (시간 포함)
                                tempScheduleData.startDate = dateTimeFormat.parse(dateStart)
                                tempScheduleData.startIsIncludeTime = true
                            }

                            if(dateEnd != null) {
                                if(dateEnd.trim().length == 10) {
                                    // 2022-05-06 (시간 미포함)
                                    tempScheduleData.endDate = dateFormat.parse(dateEnd)
                                    tempScheduleData.endIsIncludeTime = false
                                } else {
                                    // 2022-05-06T20:00:00.000+09:00 (시간 포함)
                                    tempScheduleData.endDate = dateTimeFormat.parse(dateEnd)
                                    tempScheduleData.endIsIncludeTime = true
                                }
                            }

                            scheduleDataList.add(tempScheduleData)
                        }
                    }
                    addEventOnCalendar()



                    targetEventList.clear()


                    // 데이터 필터링

                    // 1일 일정 (끝나는 날이 없을때) -> 같은날인것만 필터링
                    val filteredData1 = scheduleDataList.filter { scheduleData ->
                        (scheduleData.endDate == null) && Utils.isEqualDate(scheduleData.startDate!!.time, binding.cvLabSchedule.selectedDate.timeInMillis)
                    }

                    val filteredData2 = scheduleDataList.filter { scheduleData ->
                        if(scheduleData.endDate != null) {
                            val startDate = Calendar.getInstance().apply {
                                timeInMillis = scheduleData.startDate!!.time
                                set(Calendar.HOUR, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            val endDate = Calendar.getInstance().apply {
                                timeInMillis = scheduleData.endDate!!.time
                                set(Calendar.HOUR, 23)
                                set(Calendar.MINUTE, 59)
                                set(Calendar.SECOND, 59)
                            }
                            startDate.timeInMillis <= binding.cvLabSchedule.selectedDate.timeInMillis && binding.cvLabSchedule.selectedDate.timeInMillis <= endDate.timeInMillis
                        } else false
                    }
                    // 2일 이상 일정
                    targetEventList.addAll(filteredData1)
                    targetEventList.addAll(filteredData2)

                    scheduleAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NotionDatabaseQueryResponse>, t: Throwable) {

            }
        })
    }
}