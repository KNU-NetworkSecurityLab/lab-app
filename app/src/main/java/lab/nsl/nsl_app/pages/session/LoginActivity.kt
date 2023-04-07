package lab.nsl.nsl_app.pages.session

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.databinding.ActivityLoginBinding
import lab.nsl.nsl_app.pages.BluetoothTestActivity
import lab.nsl.nsl_app.pages.MainBaseActivity
import lab.nsl.nsl_app.utils.ParentActivity
import lab.nsl.nsl_app.utils.SharedPreferenceHelper
import lab.nsl.nsl_app.utils.Utils
import lab.nsl.nsl_app.utils.nslAPI.LoginRequestDTO
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
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


        autoLogin()
    }

    private fun autoLogin() {
        if (SharedPreferenceHelper.isAutoLoginEnable(applicationContext)) {
            CoroutineScope(Dispatchers.Main).launch {
                val autoLoginResult = login(
                    SharedPreferenceHelper.getAutoLoginID(applicationContext)!!,
                    SharedPreferenceHelper.getAutoLoginPassword(applicationContext)!!
                )

                if (!autoLoginResult) {
                    showShortToast(Utils.getLoadingMessage())
                    SharedPreferenceHelper.setAutoLoginEnable(applicationContext, false)
                }
            }
        }
    }


    private suspend fun login(studentID: String, password: String): Boolean {
        val loginRequestDTO = LoginRequestDTO(studentID, password)
        val loginCall = nslAPI.loginCall(loginRequestDTO)

        showProgress(this@LoginActivity, getString(R.string.msg_wait))
        val response = loginCall.awaitResponse()
        hideProgress()

        return if (response.isSuccessful) {
            // 자동로그인 체크
            if (!SharedPreferenceHelper.isAutoLoginEnable(applicationContext) && binding.ckLoginAuto.isChecked) {
                SharedPreferenceHelper.setAutoLoginEnable(applicationContext, true)
                SharedPreferenceHelper.setAutoLoginID(applicationContext, studentID)
                SharedPreferenceHelper.setAutoLoginPassword(applicationContext, password)
            }

            // 토큰
            val authorizationToken = response.body()!!.string()

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