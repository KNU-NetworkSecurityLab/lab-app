package com.example.nsl_app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nsl_app.databinding.FragmentScheduleCalendarBinding
import com.example.nsl_app.databinding.FragmentScheduleMeetingBinding

class ScheduleCalendarFragment : Fragment() {
    private lateinit var binding:FragmentScheduleCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleCalendarBinding.inflate(inflater, container, false)


        binding.run {
            btnCalAdd.setOnClickListener {
                val intent = Intent(activity, ScheduleAddActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }
}