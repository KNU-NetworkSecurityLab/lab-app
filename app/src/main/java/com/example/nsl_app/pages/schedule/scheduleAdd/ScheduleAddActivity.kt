package com.example.nsl_app.pages.schedule.scheduleAdd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityScheduleAddBinding
import com.example.nsl_app.pages.schedule.scheduleAdd.tagAddPopup.ScheduleAddTagDialogFragment
import com.example.nsl_app.pages.schedule.scheduleAdd.tagAddPopup.TagAddClickListener
import com.example.nsl_app.utils.notionAPI.NotionAPI
import com.example.nsl_app.utils.notionAPI.NotionDatabaseQueryResponse
import com.example.nsl_app.utils.notionAPI.NotionRetrieveDatabaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAddBinding
    private lateinit var tagAdapter: SchTagAdapter
    private val tags = ArrayList<String>()
    private val selectedTags = ArrayList<String>()
    private val scheduleAddTagDialogFragment = ScheduleAddTagDialogFragment()

    private val dateFormat = SimpleDateFormat("yyyy년 M월 d일")
    private val timeFormat = SimpleDateFormat("a hh:mm")

    private val startDay = Calendar.getInstance()
    private val endDay = Calendar.getInstance()

    private val tagClickListener = object : TagAddClickListener {
        override fun onTagClick(tag: String) {
            scheduleAddTagDialogFragment.dismiss()

            selectedTags.add(tag)
            tagAdapter.notifyDataSetChanged()

            tags.remove(tag)
        }
    }

    private val tagRemoveClickListener = object : SchTagAdapter.TagRemoveClickListener {
        override fun onRemoveClick(tag: String) {
            tags.add(tag)
            tags.sort()

            selectedTags.remove(tag)
            tagAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarScheduleAdd)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        getTags()

        startDay[Calendar.MINUTE] = 0
        startDay[Calendar.MILLISECOND] = 0
        endDay[Calendar.MINUTE] = 0
        endDay[Calendar.MILLISECOND] = 0

        tagAdapter = SchTagAdapter(applicationContext, selectedTags)
        tagAdapter.tagRemoveClickListener = tagRemoveClickListener

        binding.run {
            tvCalTagAdd.setOnClickListener {
                scheduleAddTagDialogFragment.tags = tags
                scheduleAddTagDialogFragment.tagAddClickListener = tagClickListener
                scheduleAddTagDialogFragment.show(supportFragmentManager, "NewCalendarTagAdd")
            }

            listSchTags.adapter = tagAdapter
            listSchTags.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            tagAdapter.notifyDataSetChanged()


            ckSchEntireDay.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    tvCalAddStartTime.visibility = View.GONE
                    tvCalAddEndTime.visibility = View.GONE
                } else {
                    tvCalAddStartTime.visibility = View.VISIBLE
                    tvCalAddEndTime.visibility = View.VISIBLE
                }
            }


            tvCalAddStartDay.text = dateFormat.format(startDay.timeInMillis)
            tvCalAddStartTime.text = timeFormat.format(startDay.timeInMillis)
            tvCalAddEndDay.text = dateFormat.format(endDay.timeInMillis)
            tvCalAddEndTime.text = timeFormat.format(endDay.timeInMillis)

            tvCalAddStartDay.setOnClickListener {
                val datePickerDialog = DatePickerDialog(this@ScheduleAddActivity,
                    R.style.customDatePickerTheme,
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                        startDay[Calendar.YEAR] = year
                        startDay[Calendar.MONTH] = month
                        startDay[Calendar.DAY_OF_MONTH] = day

                        tvCalAddStartDay.text = dateFormat.format(startDay.timeInMillis)

                    }, startDay[Calendar.YEAR], startDay[Calendar.MONTH], startDay[Calendar.DAY_OF_MONTH])
                datePickerDialog.show()
            }

            tvCalAddStartTime.setOnClickListener {
                val timePickerDialog = TimePickerDialog(this@ScheduleAddActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        startDay[Calendar.HOUR] = hour
                        startDay[Calendar.MINUTE] = minute

                        tvCalAddStartTime.text = timeFormat.format(startDay.timeInMillis)

                    },startDay[Calendar.HOUR], startDay[Calendar.MINUTE],false)
                timePickerDialog.show()
            }

            tvCalAddEndDay.setOnClickListener {
                val datePickerDialog = DatePickerDialog(this@ScheduleAddActivity,
                    R.style.customDatePickerTheme,
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                        endDay[Calendar.YEAR] = year
                        endDay[Calendar.MONTH] = month
                        endDay[Calendar.DAY_OF_MONTH] = day

                        tvCalAddEndDay.text = dateFormat.format(endDay.timeInMillis)

                    }, endDay[Calendar.YEAR], endDay[Calendar.MONTH], endDay[Calendar.DAY_OF_MONTH])
                datePickerDialog.show()
            }

            tvCalAddEndTime.setOnClickListener {
                val timePickerDialog = TimePickerDialog(this@ScheduleAddActivity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        endDay[Calendar.HOUR] = hour
                        endDay[Calendar.MINUTE] = minute

                        tvCalAddEndTime.text = timeFormat.format(endDay.timeInMillis)

                    },endDay[Calendar.HOUR], endDay[Calendar.MINUTE],false)
                timePickerDialog.show()
            }
        }
    }

    private fun getTags() {
        val notionAPI = NotionAPI.create()
        val tagCall = notionAPI.getNotionRetrieveData(
            NotionAPI.NOTION_DB_SCHEDULE_ID,
            NotionAPI.notionVersion,
            getString(R.string.secret_notion_key))

        tagCall.enqueue(object : Callback<NotionRetrieveDatabaseResponse> {
            override fun onResponse(
                call: Call<NotionRetrieveDatabaseResponse>,
                response: Response<NotionRetrieveDatabaseResponse>
            ) {

                if(response.isSuccessful) {
                    val body = response.body() as NotionRetrieveDatabaseResponse
                    body.properties.태그.multi_select.options.forEach {
                        tags.add(it.name)
                    }
                } else {
                    Toast.makeText(applicationContext, response.errorBody()!!.string(),Toast.LENGTH_SHORT).show()
                    Log.d("devvv",response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<NotionRetrieveDatabaseResponse>, t: Throwable) {
            }
        })
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}