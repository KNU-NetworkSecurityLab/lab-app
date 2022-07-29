package com.example.nsl_app.pages.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.adapters.BookImageAdapter
import com.example.nsl_app.databinding.ActivityBookAddBinding

class BookAddActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val MAX_IMAGE_COUNT = 5
    }

    private val binding by lazy { ActivityBookAddBinding.inflate(layoutInflater) }
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUriList = ArrayList<Uri>()
    private val imageAdapter by lazy { BookImageAdapter(applicationContext, imageUriList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolbarSetUp()
        initGetImage()

        binding.run {
            rvBookPhotos.run {
                adapter = imageAdapter
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
            imageAdapter.onImageRemoveClickListener =
                object : BookImageAdapter.OnImageRemoveClickListener {
                    override fun onRemove(position: Int) {
                        imageUriList.removeAt(position)
                        refreshImage()
                    }
                }
            refreshImage()

            btnBookAddPicture.setOnClickListener {
                // startActivityForResult 가 메모리 관련 문제로 Deprecated (사용 가능하긴 하지만 비권장) 됨
                // 개선판인 activityResultLauncher 와 registerForActivityResult 를 사용하는 것이 권장

                if (imageUriList.size >= MAX_IMAGE_COUNT) {
                    Toast.makeText(
                        applicationContext,
                        R.string.msg_max_image_count_exceed,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                activityResultLauncher.launch(intent)
            }


            btnBookAddFinish.setOnClickListener {
                finish()
            }
        }
    }

    private fun refreshImage() {
        imageAdapter.notifyDataSetChanged()

        binding.rvBookPhotos.visibility = if (imageUriList.size == 0) View.GONE else View.VISIBLE

        binding.run {
            btnBookAddPicture.text =
                "${getString(R.string.obj_book_add_picture)} (${imageUriList.size}/${MAX_IMAGE_COUNT})"
        }
    }

    private fun initGetImage() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK && it.data != null) {
                    val currentImageUri = it.data?.data
                    try {
                        imageUriList.add(currentImageUri!!)
                        refreshImage()
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