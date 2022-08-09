package com.example.nsl_app.pages.introduce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nsl_app.databinding.ActivityIntroduceBinding

class IntroduceActivity : AppCompatActivity() {
    private val binding by lazy { ActivityIntroduceBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarIntroduce)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
    }
}