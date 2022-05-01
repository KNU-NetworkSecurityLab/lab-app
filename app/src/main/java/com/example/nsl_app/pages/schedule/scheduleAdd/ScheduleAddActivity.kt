package com.example.nsl_app.pages.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.nsl_app.databinding.ActivityScheduleAddBinding
import com.example.nsl_app.utils.TagClickListener

class ScheduleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAddBinding




    private val tagClickListener = object : TagClickListener {
        override fun onTagClick(tag: String) {
            Toast.makeText(applicationContext,"$tag 선택됨",Toast.LENGTH_SHORT).show()
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



        binding.run {
            tvCalTagAdd.setOnClickListener {
                val scheduleAddTagDialogFragment = ScheduleAddTagDialogFragment().newInstance(tagClickListener)

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