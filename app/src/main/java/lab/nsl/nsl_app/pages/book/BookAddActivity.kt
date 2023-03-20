package lab.nsl.nsl_app.pages.book

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.adapters.BookRegisterImageAdapter
import lab.nsl.nsl_app.adapters.RemovableLabelAdapter
import lab.nsl.nsl_app.databinding.ActivityBookAddBinding
import lab.nsl.nsl_app.utils.SharedPreferenceHelper
import lab.nsl.nsl_app.utils.Utils
import lab.nsl.nsl_app.utils.nslAPI.BookRequestDTO
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.awaitResponse
import java.io.File

open class BookAddActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val MAX_IMAGE_COUNT = 5
    }

    protected val TAG = "BookAddActivity"
    protected val binding by lazy { ActivityBookAddBinding.inflate(layoutInflater) }
    protected lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    protected var imageUriList = ArrayList<Uri>()
    protected val imageAdapter by lazy { BookRegisterImageAdapter(applicationContext, imageUriList) }
    protected val nslAPI by lazy { NSLAPI.create() }
    protected val tagsList = ArrayList<String>()
    protected val tagsAdapter by lazy { RemovableLabelAdapter(applicationContext, tagsList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        commonInit()

        binding.btnBookAddFinish.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                bookUpload()
            }
        }
    }

    protected fun commonInit() {
        toolbarSetUp()
        initGetImage()
        initTagsList()

        binding.run {
            rvBookPhotos.run {
                adapter = imageAdapter
                layoutManager =
                    LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
            }
            imageAdapter.onImageRemoveClickListener =
                object : BookRegisterImageAdapter.OnImageRemoveClickListener {
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

                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                activityResultLauncher.launch(intent)
            }
        }
    }

    private fun initTagsList() {
        binding.run {
            rvBookTags.adapter = tagsAdapter

            btnBookAddTag.setOnClickListener {
                tagsList.add(etBookAddTag.text.toString())
                etBookAddTag.setText("")
                tagsAdapter.notifyDataSetChanged()

                svBookAdd.smoothScrollTo(0, svBookAdd.bottom)
            }
        }
    }

    private suspend fun bookUpload() {
        val bookRequestDTO = BookRequestDTO(
            binding.etBookTitle.text.toString(),
            binding.etBookAuthor.text.toString(),
            binding.etBookPublish.text.toString(),
            tagsList
        )

        val requestImages = ArrayList<MultipartBody.Part>()

        imageUriList.forEach {
            val imageFile = File(Utils.getRealPathFromURI(applicationContext, it))
            val requestFile = RequestBody.create(
                contentResolver.getType(imageUriList[0])!!.toMediaTypeOrNull(), imageFile)
            val body = MultipartBody.Part.createFormData("bookImages", imageFile.name, requestFile)

            requestImages.add(body)
        }


        val bookRegisterCall = nslAPI.bookRegisterCall(
            SharedPreferenceHelper.getAuthorizationToken(applicationContext)!!,
            bookRequestDTO,
            requestImages
        ).awaitResponse()

        if (bookRegisterCall.isSuccessful) {
            Log.d(TAG, "bookUpload: ${bookRegisterCall.body()!!.string()}")
            Toast.makeText(applicationContext, "책 등록 성공", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            bookRegisterCall.body()?.toString()?.let { Log.d(TAG, "bookUpload: $it") }
            Toast.makeText(applicationContext, "책 등록 실패", Toast.LENGTH_SHORT).show()
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
                    // get image uri from intent
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