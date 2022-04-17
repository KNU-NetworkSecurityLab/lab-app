package com.example.nsl_app

import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.nsl_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getRealSize(size)

        val deviceWidth = size.x
        val containerSize = (deviceWidth * 0.35).toInt()

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
        }

        return binding.root
    }


}