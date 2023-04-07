package lab.nsl.nsl_app.pages

import android.Manifest
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import lab.nsl.nsl_app.databinding.ActivityBluetoothTestBinding
import java.io.IOException
import java.util.*

class BluetoothTestActivity : AppCompatActivity() {
    private val TAG = "BluetoothTestActivity"
    private val binding by lazy { ActivityBluetoothTestBinding.inflate(layoutInflater) }

    private val arduinoUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID for SPP profile

    private val bluetoothAdapter by lazy { bluetoothManager.adapter }
    private var bluetoothSocket: BluetoothSocket? = null

    private val bluetoothManager by lazy { getSystemService(BluetoothManager::class.java) }


    private lateinit var broadcastReceiver: BroadcastReceiver

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Toast.makeText(this, "블루투스가 활성화 되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "블루투스가 활성화 되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnDev.setOnClickListener {
            connectToArduino()
        }

        binding.btnSend.setOnClickListener {
            val outputStream = bluetoothSocket?.outputStream
            outputStream?.write(binding.etDev.text.toString().toByteArray())
        }
    }

    // function to connect to the Arduino
    private fun connectToArduino() {
        val device = bluetoothAdapter.getRemoteDevice("98:D3:31:FB:42:F4")

        // open a Bluetooth socket to the Arduino device
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(arduinoUUID)
            bluetoothSocket?.connect()
            Toast.makeText(this, "Connected to Arduino", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Could not connect to Arduino", Toast.LENGTH_SHORT).show()
        }
    }

    // function to disconnect from the Arduino
    private fun disconnectFromArduino() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}

