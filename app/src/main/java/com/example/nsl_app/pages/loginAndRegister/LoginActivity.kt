package com.example.nsl_app.pages.loginAndRegister

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityLoginBinding
import com.example.nsl_app.pages.MainBaseActivity
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.SoftKeyboard
import com.example.nsl_app.utils.nslAPI.LoginRequestDTO
import com.example.nsl_app.utils.nslAPI.NSLAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ParentActivity() {
    private val nslAPI by lazy { NSLAPI.create() }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var softKeyboard: SoftKeyboard


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.run {
            btnLogin.setOnClickListener {

                if(binding.etLoginNumber.text.toString().isEmpty()) {
                    Toast.makeText(applicationContext,getString(R.string.msg_login_need_id),Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(binding.etLoginPw.text.toString().isEmpty()) {
                    Toast.makeText(applicationContext,getString(R.string.msg_login_need_pw),Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val loginRequestDTO = LoginRequestDTO(binding.etLoginNumber.text.toString(), binding.etLoginPw.text.toString())
                val loginCall = nslAPI.loginCall(loginRequestDTO)

                showProgress(this@LoginActivity, getString(R.string.msg_wait))

                loginCall.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        hideProgress()

                        if(response.isSuccessful) {
                            val authorizationToken = response.headers()[getString(R.string.glb_authorization)].toString()
                            SharedPreferenceHelper.setAuthorizationToken(applicationContext, authorizationToken)

                            val intent = Intent(this@LoginActivity, MainBaseActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, getString(R.string.msg_login_fail), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(applicationContext, getString(R.string.glb_internet_error), Toast.LENGTH_SHORT).show()
                        hideProgress()
                    }
                })
            }


            tvSignUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }

    }

}