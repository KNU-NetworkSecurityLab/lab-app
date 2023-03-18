package com.example.nsl_app.pages.session

import android.content.Intent
import android.os.Bundle
import com.example.nsl_app.databinding.ActivitySplashBinding
import com.example.nsl_app.pages.MainBaseActivity
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.nslAPI.LoginRequestDTO
import com.example.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class SplashActivity : ParentActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private val nslAPI by lazy { NSLAPI.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            if (loginCall()) {
                startActivity(Intent(this@SplashActivity, MainBaseActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
    }


    private suspend fun loginCall(): Boolean {
        if (SharedPreferenceHelper.isAutoLoginEnable(applicationContext)) {

            val id = SharedPreferenceHelper.getAutoLoginID(applicationContext)
            val password = SharedPreferenceHelper.getAutoLoginPassword(applicationContext)

            if (id == null || password == null) {
                showShortToast("자동 로그인 에러")
                SharedPreferenceHelper.setAutoLoginEnable(applicationContext, false)
                return false
            }

            val response = nslAPI.loginCall(LoginRequestDTO(id, password)).awaitResponse()

            if (response.isSuccessful) {
                response.body()?.let {
                    SharedPreferenceHelper.setAuthorizationToken(applicationContext, it.string())
                    return true
                }
            }
        }

        return false
    }
}