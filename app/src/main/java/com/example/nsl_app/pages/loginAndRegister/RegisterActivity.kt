package com.example.nsl_app.pages.loginAndRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            setSupportActionBar(binding.toolbarRegister)
            supportActionBar?.run {
                // 앱 바 뒤로가기 버튼 설정
                setDisplayHomeAsUpEnabled(true)
            }


            btnRegFinish.setOnClickListener { finish() }
        }
    }

    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 앱 바 클릭 이벤트
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}