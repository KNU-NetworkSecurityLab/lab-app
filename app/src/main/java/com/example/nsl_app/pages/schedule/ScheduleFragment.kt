package com.example.nsl_app.pages.schedule

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nsl_app.R
import com.example.nsl_app.databinding.FragmentScheduleBinding
import com.example.nsl_app.databinding.FragmentScheduleMeetingBinding
import com.example.nsl_app.pages.schedule.scheduleAdd.ScheduleAddActivity


class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
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
}