package com.example.nsl_app.pages.home

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nsl_app.databinding.FragmentHomeBinding
import com.example.nsl_app.pages.book.BookActivity
import com.example.nsl_app.pages.member.MemberActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getRealSize(size)

        val deviceWidth = size.x
        val containerSize = (deviceWidth * 0.4).toInt()

        binding.run {
            containerSmartDoor.layoutParams.run {
                height = containerSize
                width = containerSize
            }
            containerSmartCoffee.layoutParams.run {
                height = containerSize
                width = containerSize
            }
            containerSmartLight.layoutParams.run {
                height = containerSize
                width = containerSize
            }
            containerSmartPaper.layoutParams.run {
                height = containerSize
                width = containerSize
            }

            btnHomeBooks.setOnClickListener {
                val intent = Intent(requireActivity(), BookActivity::class.java)
                startActivity(intent)
            }

            btnHomeLabMember.setOnClickListener {
                startActivity(Intent(requireActivity(), MemberActivity::class.java))
            }
        }

        return binding.root
    }
}