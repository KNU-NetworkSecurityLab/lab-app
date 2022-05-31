package com.example.nsl_app.utils.testRoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nsl_app.databinding.FragmentScheduleBinding

class ViewPageSample : Fragment() {
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = FragmentScheduleBinding.inflate(inflater, container, false)
//
//        val fragmentPagerAdapter = FragmentPagerAdapter(requireActivity())
//        fragmentPagerAdapter.addFragment(ScheduleCalendarFragment())
//        fragmentPagerAdapter.addFragment(ScheduleMeetingFragment())
//
//
//        binding.run {
//            viewPagerSchedule.adapter = fragmentPagerAdapter
//            viewPagerSchedule.registerOnPageChangeCallback(object :
//                ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//
//                }
//            })
//
//            TabLayoutMediator(tabLayoutSchedule, viewPagerSchedule) { tab, position ->
//                tab.text = when(position) {
//                    0 -> getString(R.string.obj_sch_tab0)
//                    1 -> getString(R.string.obj_sch_tab1)
//                    else -> ""
//                }
//            }.attach()
//        }
        return binding.root
    }
}