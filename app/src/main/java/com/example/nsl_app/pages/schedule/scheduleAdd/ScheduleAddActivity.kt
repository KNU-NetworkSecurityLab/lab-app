package com.example.nsl_app.pages.schedule.scheduleAdd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.nsl_app.databinding.ActivityScheduleAddBinding

class ScheduleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAddBinding
    private val tags = ArrayList<String>()
    private val selectedTags = ArrayList<String>()
    private val scheduleAddTagDialogFragment = ScheduleAddTagDialogFragment()

    private val tagClickListener = object : TagClickListener {
        override fun onTagClick(tag: String) {
            Toast.makeText(applicationContext,"$tag 선택됨",Toast.LENGTH_SHORT).show()

            scheduleAddTagDialogFragment.dismiss()
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



        binding.run {
            tvCalTagAdd.setOnClickListener {
                scheduleAddTagDialogFragment.tags = tags
                scheduleAddTagDialogFragment.tagClickListener = tagClickListener

                scheduleAddTagDialogFragment.show(supportFragmentManager,"NewCalendarTagAdd")
            }
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