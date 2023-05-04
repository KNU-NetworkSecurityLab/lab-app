package lab.nsl.nsl_app.pages.home

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.hardware.lights.LightState
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.databinding.FragmentHomeBinding
import lab.nsl.nsl_app.models.iot.DoorState
import lab.nsl.nsl_app.models.iot.NslLightState
import lab.nsl.nsl_app.models.iot.TemperatureHumidity
import lab.nsl.nsl_app.pages.book.BookListActivity
import lab.nsl.nsl_app.pages.introduce.IntroduceActivity
import lab.nsl.nsl_app.pages.member.MemberActivity
import lab.nsl.nsl_app.utils.ParentFragment
import lab.nsl.nsl_app.utils.SharedPreferenceHelper
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    var lightState = false
    var iotImportComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        getIoTinfo()

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

                if (!iotImportComplete) {
                    return@setOnCheckedChangeListener
                }

                if (bluetoothSocket == null) {
                    showShortToast("연결된 기기가 없습니다.")
                    swHomeSmartLight.isChecked = !isChecked
                    return@setOnCheckedChangeListener
                }

                lightState = isChecked

                if (isChecked) {
                    val outputStream = bluetoothSocket?.outputStream
                    outputStream?.write("1".toByteArray())
                    lightViewChange()
                } else {
                    val outputStream = bluetoothSocket?.outputStream
                    outputStream?.write("0".toByteArray())
                    lightViewChange()
                }
            }

            btnConnectBluetoothSmartLight.setOnClickListener {
                // 아두이노 연결 버튼은 3초에 한 번씩만 클릭할 수 있게 함
                if (System.currentTimeMillis() - connectClickLastTime < 3000) return@setOnClickListener

                connectClickLastTime = System.currentTimeMillis()

                if (bluetoothSocket == null) {
                    permissionCheck()
                } else {
                    showProgress(requireActivity(), "기기 연결 해제 중...")
                    disconnectFromArduino()
                    bluetoothSocket = null
                }
            }
        }
    }

    // permission check
    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            CoroutineScope(Dispatchers.Main).launch {
                connectToArduino()
            }
            showProgress(requireActivity(), "기기 연결 중...")
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            showShortToast("권한이 없으면 기능을 사용할 수 없습니다.")
        }
    }

    private fun permissionCheck() {
        TedPermission.create().setPermissionListener(permissionListener)
            .setDeniedMessage("[설정] > [권한] 에서 권한 허용을 할 수 있습니다").setPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT
            ).check()
    }

    // function to connect to the Arduino
    @SuppressLint("MissingPermission")
    private fun connectToArduino() {
        val device = bluetoothAdapter.getRemoteDevice("98:D3:31:FB:42:F4")

        // open a Bluetooth socket to the Arduino device
        try {
            //bluetoothSocket = device.createRfcommSocketToServiceRecord(arduinoUUID)
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(arduinoUUID)

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


    private fun getIoTinfo() {
        nslAPI.getDoorStateCall(token).enqueue(object : Callback<DoorState> {
            override fun onResponse(call: Call<DoorState>, response: Response<DoorState>) {
                if (response.isSuccessful) {
                    val doorState = response.body()!!
                    if (doorState.isDoorOpen) {
                        binding.tvIotDoor.text = "문 열림"
                        binding.tvIotDoor.compoundDrawableTintList =
                            resources.getColorStateList(R.color.main_color)
                    } else {
                        binding.tvIotDoor.text = "문 닫힘"
                        binding.tvIotDoor.compoundDrawableTintList =
                            resources.getColorStateList(R.color.iot_off_color)
                    }
                } else showShortToast("문 상태를 불러오는데 실패했습니다.")
            }

            override fun onFailure(call: Call<DoorState>, t: Throwable) {
                showShortToast("문 상태를 불러오는데 실패했습니다.")
            }
        })


        nslAPI.getTemperatureHumidityCall(token).enqueue(object : Callback<TemperatureHumidity> {
            override fun onResponse(
                call: Call<TemperatureHumidity>, response: Response<TemperatureHumidity>
            ) {
                if (response.isSuccessful) {
                    val temperatureHumidity = response.body()!!
                    binding.tvIotTemp.text = "온도: ${temperatureHumidity.temperature} ℃"
                    binding.tvIotHumidity.text = "습도: ${temperatureHumidity.humidity} %"
                } else showShortToast("온습도를 불러오는데 실패했습니다.")
            }

            override fun onFailure(call: Call<TemperatureHumidity>, t: Throwable) {
                showShortToast("온습도를 불러오는데 실패했습니다.")
            }
        })


        nslAPI.getLightStateCall(token).enqueue(object : Callback<NslLightState> {
            override fun onResponse(call: Call<NslLightState>, response: Response<NslLightState>) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    lightState = body.isLightOn
                    binding.swHomeSmartLight.isChecked = body.isLightOn
                    lightViewChange()
                    iotImportComplete = true
                } else {
                    showShortToast("조명 상태를 불러오는데 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<NslLightState>, t: Throwable) {
                showShortToast("조명 상태를 불러오는데 실패했습니다.")
            }
        })
    }

    fun lightViewChange() {
        if(lightState) {
            binding.tvIotLight.text = "불 켜짐"
            binding.tvIotLight.compoundDrawableTintList =
                resources.getColorStateList(R.color.main_color)
        } else {
            binding.tvIotLight.text = "불 꺼짐"
            binding.tvIotLight.compoundDrawableTintList =
                resources.getColorStateList(R.color.iot_off_color)
        }
    }
}