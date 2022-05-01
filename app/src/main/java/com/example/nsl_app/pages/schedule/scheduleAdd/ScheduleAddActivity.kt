package com.example.nsl_app.pages.schedule.scheduleAdd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.databinding.ActivityScheduleAddBinding
import com.example.nsl_app.pages.schedule.scheduleAdd.tagAddPopup.ScheduleAddTagDialogFragment
import com.example.nsl_app.pages.schedule.scheduleAdd.tagAddPopup.TagAddClickListener

class ScheduleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAddBinding
    private lateinit var tagAdapter: SchTagAdapter
    private val tags = ArrayList<String>()
    private val selectedTags = ArrayList<String>()
    private val scheduleAddTagDialogFragment = ScheduleAddTagDialogFragment()

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

        tags.add("태그1")
        tags.add("태그2")
        tags.add("태그3")
        tags.add("태그4")

        tagAdapter = SchTagAdapter(applicationContext, selectedTags)
        tagAdapter.tagRemoveClickListener = tagRemoveClickListener

        binding.run {
            tvCalTagAdd.setOnClickListener {
                scheduleAddTagDialogFragment.tags = tags
                scheduleAddTagDialogFragment.tagAddClickListener = tagClickListener
                scheduleAddTagDialogFragment.show(supportFragmentManager,"NewCalendarTagAdd")
            }

            listSchTags.adapter = tagAdapter
            listSchTags.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            tagAdapter.notifyDataSetChanged()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}