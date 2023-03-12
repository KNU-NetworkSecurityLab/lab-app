package com.example.nsl_app.pages.session

import android.content.Intent
import android.os.Bundle
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityLoginBinding
import com.example.nsl_app.pages.MainBaseActivity
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.nslAPI.LoginRequestDTO
import com.example.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class LoginActivity : ParentActivity() {
    private val nslAPI by lazy { NSLAPI.create() }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.run {
            btnLogin.setOnClickListener {
                if (binding.etLoginNumber.text.toString().isEmpty()) {
                    showShortToast(getString(R.string.msg_login_need_id))
                    return@setOnClickListener
                }

                if (binding.etLoginPw.text.toString().isEmpty()) {
                    showShortToast(getString(R.string.msg_login_need_pw))
                    return@setOnClickListener
                }

                CoroutineScope(Dispatchers.Main).launch {
                    login(binding.etLoginNumber.text.toString(), binding.etLoginPw.text.toString())
                }
            }


            tvSignUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
    }


    private suspend fun login(studentID: String, password: String): Boolean {

        // 코루틴
        val loginRequestDTO = LoginRequestDTO(studentID, password)
        val loginCall = nslAPI.loginCall(loginRequestDTO)

        showProgress(this@LoginActivity, getString(R.string.msg_wait))
        val response = loginCall.awaitResponse()
        hideProgress()

        return if (response.isSuccessful) {
            // 토큰
            val authorizationToken =
                response.headers()[getString(R.string.glb_authorization)].toString()

            // 토큰 저장
            SharedPreferenceHelper.setAuthorizationToken(applicationContext, authorizationToken)

            val intent = Intent(this@LoginActivity, MainBaseActivity::class.java)
            startActivity(intent)
            finish()
            true
        } else {
            showShortToast(getString(R.string.msg_login_fail))
            false
        }
    }
}