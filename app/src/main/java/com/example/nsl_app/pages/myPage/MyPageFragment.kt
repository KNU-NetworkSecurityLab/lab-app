package com.example.nsl_app.pages.myPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nsl_app.R
import com.example.nsl_app.databinding.FragmentMyPageBinding
import com.example.nsl_app.databinding.FragmentScheduleMeetingBinding

class MyPageFragment : Fragment() {
    private lateinit var binding : FragmentMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)





        return binding.root
    }
}