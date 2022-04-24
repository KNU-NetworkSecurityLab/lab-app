package com.example.nsl_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.nsl_app.Utils.FragmentPagerAdapter
import com.example.nsl_app.databinding.FragmentScheduleBinding

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

        val fragmentPagerAdapter = FragmentPagerAdapter(requireActivity())
        fragmentPagerAdapter.addFragment(ScheduleCalendarFragment())
        fragmentPagerAdapter.addFragment(ScheduleMeetingFragment())


        binding.run {
            viewPagerSchedule.adapter = fragmentPagerAdapter
            viewPagerSchedule.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                }
            })
        }



        return binding.root
    }


}