package com.example.nsl_app.pages.loginAndRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.nsl_app.R
import com.example.nsl_app.databinding.ActivityRegisterBinding
import com.example.nsl_app.utils.nslAPI.NSLAPI
import com.example.nsl_app.utils.nslAPI.SignUpRequestDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
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
//                if (binding.etRegPw.text.toString() != binding.etRegPwCheck.text.toString()) {
//                    Toast.makeText(
//                        applicationContext,
//                        getString(R.string.msg_sign_up_pw_not_checked),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }

                val signUpRequestDTO = SignUpRequestDTO(
                    userId = binding.etRegNumber.text.toString(), // id
                    name = binding.etRegName.text.toString(), // name
                    password = binding.etRegPw.text.toString(), // pw
                    mail = binding.etRegEmail.text.toString(), // email
                    phone = binding.etRegPhone.text.toString(), // phone number
                    studentId = binding.etRegNumber.text.toString(), //학번
                    position = "부원" // 직책. 임시적으로 부원으로 넣어둠
                )

                val signCall = nslAPI.signUpCall(signUpRequestDTO)

                signCall.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val str = response.body()!!.string()

                            if (str == "SignUp Success") {
                                Toast.makeText(applicationContext, getString(R.string.msg_sign_up_success), Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val str = response.errorBody()!!.string()
                            Log.d("dev",str)
                            Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

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