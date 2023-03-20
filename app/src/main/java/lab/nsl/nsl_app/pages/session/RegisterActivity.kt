package lab.nsl.nsl_app.pages.session

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.databinding.ActivityRegisterBinding
import lab.nsl.nsl_app.utils.ParentActivity
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
import lab.nsl.nsl_app.utils.nslAPI.SignUpRequestDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : ParentActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val nslAPI by lazy { NSLAPI.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.run {
            setSupportActionBar(binding.toolbarRegister)
            supportActionBar?.run {
                // 앱 바 뒤로가기 버튼 설정
                setDisplayHomeAsUpEnabled(true)
            }

            // 오직 숫자만 받기 위해 edittext inputtype을 numberPassword로 해놨음
            // 패스워드라 검은 동그라미로 마스킹 되기 때문에 마스킹을 해제하는 코드 추가
            etRegPhone.transformationMethod = null

            btnRegFinish.setOnClickListener {
                val signUpRequestDTO = SignUpRequestDTO(
                    studentId = binding.etRegNumber.text.toString(), // id
                    name = binding.etRegName.text.toString(), // name
                    password = binding.etRegPw.text.toString(), // pw
                    email = binding.etRegEmail.text.toString(), // email
                    phone = binding.etRegPhone.text.toString(), // phone number
                )

                val signCall = nslAPI.signUpCall(signUpRequestDTO)

                showProgress(this@RegisterActivity, getString(R.string.msg_wait))

                signCall.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        hideProgress()
                        if (response.isSuccessful) {
                            showShortToast(getString(R.string.msg_sign_up_success))
                            finish()
                        } else {
                            val str = response.errorBody()!!.string()
                            Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        hideProgress()
                    }
                })
            }
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