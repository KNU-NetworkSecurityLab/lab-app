package com.example.nsl_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nsl_app.databinding.ActivityScheduleAddBinding

class ScheduleAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.run {
            show()
            title = getString(R.string.obj_sch_add_title)
        }
    }
}