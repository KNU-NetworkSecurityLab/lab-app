package lab.nsl.nsl_app.pages.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.nsl.nsl_app.databinding.FragmentHomeBinding
import lab.nsl.nsl_app.pages.book.BookListActivity
import lab.nsl.nsl_app.pages.introduce.IntroduceActivity
import lab.nsl.nsl_app.pages.member.MemberActivity
import lab.nsl.nsl_app.utils.ParentFragment
import lab.nsl.nsl_app.utils.SharedPreferenceHelper
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
import retrofit2.awaitResponse

class HomeFragment : ParentFragment() {
    private val nslAPI by lazy { NSLAPI.create() }
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val token by lazy { SharedPreferenceHelper.getAuthorizationToken(requireContext())!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 파이어베이스 메시지 토큰
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("DEVVVVV", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token
            Log.d("DEVVVVVDEVVVVV", "onCreateView: ${msg}")
        })


        binding.run {
            btnHomeBooks.setOnClickListener {
                val intent = Intent(requireActivity(), BookListActivity::class.java)
                startActivity(intent)
            }

            btnHomeLabMember.setOnClickListener {
                startActivity(Intent(requireActivity(), MemberActivity::class.java))
            }

            btnHomeLabShow.setOnClickListener {
                startActivity(Intent(requireActivity(), IntroduceActivity::class.java))
            }
        }


        CoroutineScope(Dispatchers.Main).launch {
            val response = nslAPI.getUserInfoCall(token).awaitResponse()
            if (response.isSuccessful) {
                val userInfo = response.body()!!
                binding.tvHomeUserName.text = "안녕하세요. ${userInfo.name}님"
            } else {
                showShortToast("사용자 정보를 불러오는데 실패했습니다.")
            }
        }

        return binding.root
    }
}