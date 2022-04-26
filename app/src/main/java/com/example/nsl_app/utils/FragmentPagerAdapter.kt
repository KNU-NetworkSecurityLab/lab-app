package com.example.nsl_app.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    var fragmentList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment:Fragment) {
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size -1)
    }

    fun removeFragment() {
        fragmentList.removeLast()
        notifyItemInserted(fragmentList.size)
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}