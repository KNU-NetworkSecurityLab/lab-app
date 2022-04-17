package com.example.nsl_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.nsl_app.databinding.ActivityMainBaseBinding
import com.google.android.material.navigation.NavigationBarView
import java.util.zip.Inflater

class MainBaseActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.run {

            supportFragmentManager.beginTransaction().add(R.id.container_main,HomeFragment()).commit()
            navMain.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, HomeFragment()).commit()
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_community -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, CommunityFragment()).commit()
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_schedule -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, ScheduleFragment()).commit()
                        return@setOnItemSelectedListener true
                    }

                    R.id.nav_my_page -> {
                        supportFragmentManager.beginTransaction().replace(R.id.container_main, MyPageFragment()).commit()
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
        }

    }
}