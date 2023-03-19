package com.example.nsl_app.pages.schedule

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
import com.example.nsl_app.adapters.SchTagAdapter
import com.example.nsl_app.adapters.SchTagAddAdapter
import com.example.nsl_app.databinding.ActivityScheduleAddBinding
import com.example.nsl_app.models.NotionMemberItem
import com.example.nsl_app.utils.Constants
import com.example.nsl_app.utils.SecretConstants
import com.example.nsl_app.utils.Utils
import com.example.nsl_app.utils.notionAPI.*
import com.example.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.*
import com.example.nsl_app.utils.notionAPI.dataDTO.registerScheduleWithContent.Properties
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAddBinding
    private lateinit var tagAdapter: SchTagAdapter
    private lateinit var personAdapter: SchTagAdapter
    private val tags = ArrayList<String>()
    private val people = ArrayList<String>()
    private val selectedTags = ArrayList<String>()
    private val selectedPeople = ArrayList<String>()
    private val scheduleAddTagDialogFragment = ScheduleAddTagDialogFragment()
    private val scheduleAddPersonDialogFragment = ScheduleAddPersonDialogFragment()

    private val dateFormat = SimpleDateFormat("yyyy년 M월 d일")
    private val dateSliderFormat = SimpleDateFormat("yyyy-MM-dd")
    private val timeFormat = SimpleDateFormat("a hh:mm")

    private val members = ArrayList<NotionMemberItem>()

    private val startDay = Calendar.getInstance()
    private val endDay = Calendar.getInstance()

    val notionAPI by lazy { NotionAPI.create() }
    val editPageID by lazy { intent.getStringExtra(Constants.INTENT_EXTRA_PAGE_ID) }

    private val tagClickListener = object : SchTagAddAdapter.TagClickListener {
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

    private val personClickListener = object : SchTagAddAdapter.TagClickListener {
        override fun onTagClick(tag: String) {
            scheduleAddPersonDialogFragment.dismiss()

            selectedPeople.add(tag)
            personAdapter.notifyDataSetChanged()

            people.remove(tag)
        }
    }

    private val personRemoveClickListener = object : SchTagAdapter.TagRemoveClickListener {
        override fun onRemoveClick(tag: String) {
            people.add(tag)
            tags.sort()

            selectedPeople.remove(tag)
            personAdapter.notifyDataSetChanged()
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

        getPeople()
        getTags()

        startDay[Calendar.MINUTE] = 0
        startDay[Calendar.MILLISECOND] = 0
        endDay[Calendar.MINUTE] = 0
        endDay[Calendar.MILLISECOND] = 0

        tagAdapter = SchTagAdapter(applicationContext, selectedTags)
        tagAdapter.tagRemoveClickListener = tagRemoveClickListener

        personAdapter = SchTagAdapter(applicationContext, selectedPeople)
        personAdapter.tagRemoveClickListener = personRemoveClickListener

        binding.run {
            tvCalTagAdd.setOnClickListener {
                scheduleAddTagDialogFragment.tags = tags
                scheduleAddTagDialogFragment.tagAddClickListener = tagClickListener
                scheduleAddTagDialogFragment.show(supportFragmentManager, "NewCalendarTagAdd")
            }

            tvCalPersonAdd.setOnClickListener {
                scheduleAddPersonDialogFragment.tags = people
                scheduleAddPersonDialogFragment.personAddClickListener = personClickListener
                scheduleAddPersonDialogFragment.show(supportFragmentManager, "NewCalendarPersonAdd")
            }

            listSchTags.adapter = tagAdapter
            listSchTags.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            tagAdapter.notifyDataSetChanged()

            listSchPerson.adapter = personAdapter
            listSchPerson.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            personAdapter.notifyDataSetChanged()


            ckIncludeTime.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    tvCalAddStartTime.visibility = View.VISIBLE
                    tvCalAddEndTime.visibility = View.VISIBLE
                } else {
                    tvCalAddStartTime.visibility = View.GONE
                    tvCalAddEndTime.visibility = View.GONE
                }
            }
            ckIncludeTime.isChecked = false


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
                        Toast.makeText(applicationContext,hour.toString(),Toast.LENGTH_SHORT).show()
                        startDay[Calendar.HOUR_OF_DAY] = timePicker.hour
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
                        endDay[Calendar.HOUR_OF_DAY] = hour
                        endDay[Calendar.MINUTE] = minute

                        tvCalAddEndTime.text = timeFormat.format(endDay.timeInMillis)

                    },endDay[Calendar.HOUR], endDay[Calendar.MINUTE],false)
                timePickerDialog.show()
            }

            btnCalAddFinish.setOnClickListener {
                registerSchedule()
            }
        }


        if(intent.getStringExtra(Constants.INTENT_EXTRA_WRITE_OR_EDIT_MODE) == Constants.INTENT_EXTRA_EDIT_MODE) {
            // 수정모드
            initEditMode()
        }
    }

    private fun initEditMode() {
        binding.run {
            btnCalAddFinish.text = getString(R.string.glb_edit_finish)

            // 노션 API 상 내용 수정은 불가능함
            etSchAddContent.visibility = View.GONE
            lineEtSchAddContent.visibility = View.GONE

            btnCalAddFinish.setOnClickListener { editSchedule() }

            val getScheduleCall = notionAPI.getScheduleInfo(editPageID!!, NotionAPI.NOTION_API_VERSION, SecretConstants.SECRET_NOTION_TOKEN)
            getScheduleCall.enqueue(object : Callback<NotionScheduleCreateResponse> {
                override fun onResponse(
                    call: Call<NotionScheduleCreateResponse>,
                    response: Response<NotionScheduleCreateResponse>
                ) {
                    if(response.isSuccessful) {
                        val body = response.body()!!
                        body.properties.태그.multi_select.forEach {
                            selectedTags.add(it.name)
                        }
                        tagAdapter.notifyDataSetChanged()

                        binding.run {
                            etSchAddTitle.setText(body.properties.이름.title[0].text.content)
                        }
                    }
                }

                override fun onFailure(call: Call<NotionScheduleCreateResponse>, t: Throwable) {

                }
            })
        }
    }

    private fun editSchedule() {
        val title = List<Title>(1){ Title(Text(content = binding.etSchAddTitle.text.toString())) }
        val date = if(binding.ckIncludeTime.isChecked) {
            // 시간 포함
            날짜(start = Utils.notionDateTimeFormat.format(startDay.timeInMillis), end = Utils.notionDateTimeFormat.format(endDay.timeInMillis))
        } else {
            // 날짜만
            if(startDay.timeInMillis == endDay.timeInMillis) {
                날짜(
                    start = dateSliderFormat.format(startDay.timeInMillis),
                    end = null
                )
            } else {
                날짜(
                    start = dateSliderFormat.format(startDay.timeInMillis),
                    end = dateSliderFormat.format(endDay.timeInMillis)
                )
            }
        }

        val tags = List<태그>(selectedTags.size) { it -> 태그(selectedTags[it]) }

        val notionEditSchedule = NotionEditSchedule(
            Properties(
                title = title,
                날짜 = date,
                참석자 = getNotionMember(),
                태그 = tags
            )
        )

        val editScheduleCall = notionAPI.editSchedule(editPageID!!, NotionAPI.NOTION_API_VERSION, SecretConstants.SECRET_NOTION_TOKEN, notionEditSchedule)

        editScheduleCall.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.msg_sch_add_finish),Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.d("Devvv",response.errorBody()!!.string())
                    Toast.makeText(applicationContext, response.errorBody()!!.string(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    private fun registerSchedule() {
        // Json data 상 한글 변수 / 클래스명이 사용됨

        val title = List<Title>(1){ Title(Text(content = binding.etSchAddTitle.text.toString())) }
        val date = if(binding.ckIncludeTime.isChecked) {
            // 시간 포함
            날짜(start = Utils.notionDateTimeFormat.format(startDay.timeInMillis), end = Utils.notionDateTimeFormat.format(endDay.timeInMillis))
        } else {
            // 날짜만
            if(startDay.timeInMillis == endDay.timeInMillis) {
                날짜(
                    start = dateSliderFormat.format(startDay.timeInMillis),
                    end = null
                )
            } else {
                날짜(
                    start = dateSliderFormat.format(startDay.timeInMillis),
                    end = dateSliderFormat.format(endDay.timeInMillis)
                )
            }
        }

        val tags = List<태그>(selectedTags.size) { it -> 태그(selectedTags[it]) }

        val content = binding.etSchAddContent.text.toString()

        val richText = RichText(Text(content),"text")
        val paragraph = Paragraph(List<RichText>(1){richText})

        val children = List<Children>(1) {Children(`object` = "block", paragraph = paragraph, type = "paragraph")}

        val notionCreateScheduleWithContentData = NotionCreateScheduleWithContentData(
            parent = Parent(NotionAPI.NOTION_SCHEDULE_DB_ID),
            Properties(
                title = title,
                날짜 = date,
                참석자 = getNotionMember(),
                태그 = tags
            ),
            children
        )


        val registerScheduleCall = notionAPI.registerScheduleWithContent(NotionAPI.NOTION_API_VERSION,
            SecretConstants.SECRET_NOTION_TOKEN,
            notionCreateScheduleWithContentData)

        registerScheduleCall.enqueue(object : Callback<NotionScheduleCreateResponse>{
                override fun onResponse(
                    call: Call<NotionScheduleCreateResponse>,
                    response: Response<NotionScheduleCreateResponse>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(applicationContext, getString(R.string.msg_sch_add_finish),Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(applicationContext, response.errorBody()!!.string(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<NotionScheduleCreateResponse>, t: Throwable) {

                }
            })
    }

    private fun getNotionMember():List<참석자>{
        val member = ArrayList<참석자>()

        selectedPeople.forEach {  selected ->
            member.add(참석자(members.find { it.name ==  selected}!!.notionID!!))
        }
        return member
    }

    private fun getTags() {
        val tagCall = notionAPI.getNotionRetrieveData(
            NotionAPI.NOTION_SCHEDULE_DB_ID,
            NotionAPI.NOTION_API_VERSION,
            SecretConstants.SECRET_NOTION_TOKEN)

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

    private fun getPeople() {
        val personCall = notionAPI.getMember(
            NotionAPI.NOTION_MEMBER_DB_ID,
            NotionAPI.NOTION_API_VERSION,
            SecretConstants.SECRET_NOTION_TOKEN
        )

        personCall.enqueue(object : Callback<NotionMemberResponse> {
            override fun onResponse(
                call: Call<NotionMemberResponse>,
                response: Response<NotionMemberResponse>
            ) {
                if(response.isSuccessful) {
                    val body = response.body() as NotionMemberResponse
                    body.results.forEach {
                        if(it.properties.Mention.people.isNotEmpty()) {
                            people.add(it.properties.Mention.people[0].name)
                            members.add(
                                NotionMemberItem(
                                    it.properties.Mention.people[0].name,
                                    it.properties.Mention.people[0].id
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NotionMemberResponse>, t: Throwable) {

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