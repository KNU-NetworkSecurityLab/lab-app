package com.example.nsl_app.pages.loginAndRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            btnRegFinish.setOnClickListener { finish() }
        }
    }
}