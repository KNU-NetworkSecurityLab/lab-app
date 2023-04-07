package lab.nsl.nsl_app.pages.home

import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.databinding.FragmentHomeBinding
import lab.nsl.nsl_app.pages.book.BookListActivity
import lab.nsl.nsl_app.pages.introduce.IntroduceActivity
import lab.nsl.nsl_app.pages.member.MemberActivity
import lab.nsl.nsl_app.utils.ParentFragment
import lab.nsl.nsl_app.utils.SharedPreferenceHelper
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
import retrofit2.awaitResponse
import java.io.IOException
import java.util.*

class HomeFragment : ParentFragment() {
    private val nslAPI by lazy { NSLAPI.create() }
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private val token by lazy { SharedPreferenceHelper.getAuthorizationToken(requireContext())!! }
    private val bluetoothAdapter by lazy { bluetoothManager.adapter }
    private var bluetoothSocket: BluetoothSocket? = null

    private val bluetoothManager by lazy { requireContext().getSystemService(BluetoothManager::class.java) }
    private val arduinoUUID: UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID for SPP profile

    private var connectClickLastTime = 0L

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
            val token = task.result
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

        initBluetooth()

        return binding.root
    }


    private fun initBluetooth() {
        binding.run {
            swHomeSmartLight.setOnCheckedChangeListener { _, isChecked ->

                connectClickLastTime = System.currentTimeMillis()


                if (bluetoothSocket == null) {
                    showShortToast("연결된 기기가 없습니다.")
                    swHomeSmartLight.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }

                if (isChecked) {
                    val outputStream = bluetoothSocket?.outputStream
                    outputStream?.write("1".toByteArray())

                    // 아이콘 색상 변경
                    tvIotLight.compoundDrawableTintList =
                        resources.getColorStateList(R.color.main_color)

                } else {
                    val outputStream = bluetoothSocket?.outputStream
                    outputStream?.write("0".toByteArray())

                    // 아이콘 색상 변경
                    tvIotLight.compoundDrawableTintList =
                        resources.getColorStateList(R.color.iot_off_color)
                }
            }

            btnConnectBluetoothSmartLight.setOnClickListener {
                // 아두이노 연결 버튼은 3초에 한 번씩만 클릭할 수 있게 함
                if(System.currentTimeMillis() - connectClickLastTime < 3000)
                    return@setOnClickListener

                if (bluetoothSocket == null) {
                    showProgress(requireActivity(), "기기 연결 중...")
                    CoroutineScope(Dispatchers.Main).launch {
                        connectToArduino()
                    }
                } else {
                    showProgress(requireActivity(), "기기 연결 해제 중...")
                    disconnectFromArduino()
                    bluetoothSocket = null
                }
            }
        }

    }


    // function to connect to the Arduino
    private fun connectToArduino() {
        val device = bluetoothAdapter.getRemoteDevice("98:D3:31:FB:42:F4")

        // open a Bluetooth socket to the Arduino device
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(arduinoUUID)
            bluetoothSocket?.connect()
            showShortToast("기기 연결됨")
            hideProgress()

            binding.btnConnectBluetoothSmartLight.imageTintList =
                resources.getColorStateList(R.color.main_color)

        } catch (e: IOException) {
            e.printStackTrace()
            hideProgress()
            showShortToast("기기 연결 실패")
            showShortToast(e.message.toString())
        }
    }

    // function to disconnect from the Arduino
    private fun disconnectFromArduino() {
        try {
            bluetoothSocket?.close()
            hideProgress()
            showShortToast("기기 연결 해제됨")

            binding.btnConnectBluetoothSmartLight.imageTintList =
                resources.getColorStateList(R.color.iot_off_color)

        } catch (e: IOException) {
            e.printStackTrace()
            hideProgress()
        }
    }


    private fun changeTextViewLeftIconColor(textView: TextView, color: Int) {
        textView.compoundDrawableTintList = resources.getColorStateList(color)
    }


}