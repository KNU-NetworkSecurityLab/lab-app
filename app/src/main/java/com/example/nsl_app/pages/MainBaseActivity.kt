package com.example.nsl_app.pages

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityMainBaseBinding
import com.example.nsl_app.pages.project.ProjectFragment
import com.example.nsl_app.pages.home.HomeFragment
import com.example.nsl_app.pages.myPage.MyPageFragment
import com.example.nsl_app.pages.schedule.ScheduleFragment

class MainBaseActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.run {

            supportFragmentManager.beginTransaction().add(R.id.container_main, HomeFragment()).commit()
            navMain.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nav_home -> {
                        // status bar color
                        setStatusBarColor(Color.WHITE)
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, HomeFragment()).commit()
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_community -> {
                        setStatusBarColor(Color.WHITE)
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, ProjectFragment()).commit()
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_schedule -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, ScheduleFragment()).commit()
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_my_page -> {
                        setStatusBarColor(Color.WHITE)
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, MyPageFragment()).commit()
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
        }

    }

    private fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
    }
}