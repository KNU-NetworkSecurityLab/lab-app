package com.example.nsl_app.pages.book

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.nsl_app.databinding.ActivityBookAddBinding

class BookAddActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val MAX_IMAGE_COUNT = 5
    }

    private val binding by lazy { ActivityBookAddBinding.inflate(layoutInflater) }
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolbarSetUp()
        initGetImage()

        binding.run {
            btnBookAddPicture.setOnClickListener {
                // startActivityForResult 가 메모리 관련 문제로 Deprecated (사용 가능하긴 하지만 비권장) 됨
                // 개선판인 activityResultLauncher 와 registerForActivityResult 를 사용하는 것이 권장

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                activityResultLauncher.launch(intent)
            }
        }
    }

    private fun initGetImage() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK && it.data != null) {
                    val currentImageUri = it.data?.data
                    try {
                        currentImageUri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    currentImageUri
                                )
                                binding.ivSample.setImageBitmap(bitmap)
                            } else {
                                val source =
                                    ImageDecoder.createSource(this.contentResolver, currentImageUri)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                binding.ivSample?.setImageBitmap(bitmap)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (it.resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("ActivityResult", "something wrong")
                }
            }
    }

    private fun toolbarSetUp() {
        setSupportActionBar(binding.toolbarBookAdd)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
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