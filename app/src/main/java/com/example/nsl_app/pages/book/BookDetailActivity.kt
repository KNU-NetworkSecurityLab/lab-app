package com.example.nsl_app.pages.book

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.example.nsl_app.R
import com.example.nsl_app.adapters.BookImageAdapter
import com.example.nsl_app.adapters.LabelAdapter
import com.example.nsl_app.databinding.ActivityBookDetailBinding
import com.example.nsl_app.models.BookDetailItem
import com.example.nsl_app.utils.Constants
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class BookDetailActivity : ParentActivity() {
    private val nslAPI by lazy { NSLAPI.create() }
    private val binding by lazy { ActivityBookDetailBinding.inflate(layoutInflater) }
    private lateinit var bookDetailItem: BookDetailItem
    private var bookId = -1
    private val token by lazy { SharedPreferenceHelper.getAuthorizationToken(applicationContext)!! }
    private val tags = ArrayList<String>()
    private val tagsAdapter by lazy { LabelAdapter(applicationContext, tags) }

    private val bitmapList = ArrayList<Bitmap>()
    private val imageAdapter by lazy { BookImageAdapter(applicationContext, bitmapList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toolbarSetUp()

        bookId = intent.getIntExtra(Constants.INTENT_EXTRA_BOOK_ID, -1)
        if (bookId == -1) {
            showShortToast("책 정보를 불러올 수 없습니다.")
            finish()
        }

        binding.run {
            rvBookTags.adapter = tagsAdapter
            tagsAdapter.textColor = getColor(R.color.mint)
            tagsAdapter.startString = "#"
            rvItemBookImages.adapter = imageAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            bookInit()
        }
    }

    private suspend fun bookInit() {

        val response = nslAPI.getBookDetailCall(token, bookId).awaitResponse()
        if (response.isSuccessful) {
            binding.run {
                bookDetailItem = response.body()!!
                tvBookAuthor.text = bookDetailItem.bookAuthor
                tvBookPublisher.text = bookDetailItem.bookPublisher
                tvBookTitle.text = bookDetailItem.bookName

                if (bookDetailItem.bookImageList.size > 0) {
                    bookDetailItem.bookImageList.forEach {
                        loadImage(it)
                    }

                    imageAdapter.notifyDataSetChanged()

                } else {
                    rvItemBookImages.visibility = View.GONE
                }

                // 태그
                if(bookDetailItem.bookTagList.size > 0) {
                    tvBookTagsEmpty.visibility = View.GONE
                    tags.addAll(bookDetailItem.bookTagList)
                    tagsAdapter.notifyDataSetChanged()
                } else {
                    tvBookTagsEmpty.visibility = View.VISIBLE
                }
            }
        } else {
            showShortToast("책 정보를 불러올 수 없습니다 2")
        }
    }

    private suspend fun loadImage(imageId: Int) {

        val response = nslAPI.getBookImageCall(token, imageId).awaitResponse()

        if (response.isSuccessful) {
            binding.run {
                val responseBody = response.body()!!
                val bitmap = BitmapFactory.decodeStream(responseBody.byteStream())
                bitmapList.add(bitmap)

//                Glide.with(applicationContext).load(image).into(ivItemBookImage)
            }
        } else {
            showShortToast("책 이미지를 불러올 수 없습니다")
        }
    }


    private fun toolbarSetUp() {
        binding.toolbarBookInfo.run {
            navigationIcon = getDrawable(R.drawable.icon_arrow_tail)
            setNavigationOnClickListener { finish() }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_post_edit -> {
                        showShortToast("수정")
//                        val intent = Intent(applicationContext, BookEditActivity::class.java)
//                        intent.putExtra(Constants.INTENT_EXTRA_BOOK_ID, bookId)
//                        startActivity(intent)
                        true
                    }
                    R.id.menu_post_delete -> {
                        // 삭제 다이얼로그

                        val dialog = AlertDialog.Builder(this@BookDetailActivity)
                            .setTitle("도서 삭제")
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setPositiveButton("삭제") { dialog, _ ->
                                dialog.dismiss()

                                CoroutineScope(Dispatchers.Main).launch {
                                    deleteBook()
                                }
                            }
                            .setNegativeButton("취소") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()

                        dialog.setOnShowListener {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                        }

                        dialog.show()

                        true
                    }
                    else -> false
                }
            }
        }
    }

    private suspend fun deleteBook() {
        val response = nslAPI.deleteBookCall(token, bookId).awaitResponse()

        if (response.isSuccessful) {
            showShortToast("삭제되었습니다")
            finish()
        } else {
            val intent = Intent(applicationContext, BookEditActivity::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_BOOK_ID, bookId)
            startActivity(intent)
        }
    }
}