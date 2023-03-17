package com.example.nsl_app.pages.book

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import com.example.nsl_app.R
import com.example.nsl_app.adapters.LabelAdapter
import com.example.nsl_app.databinding.ActivityBookInfoBinding
import com.example.nsl_app.models.BookDetailItem
import com.example.nsl_app.utils.Constants
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class BookInfoActivity : ParentActivity() {
    private val nslAPI by lazy { NSLAPI.create() }
    private val binding by lazy { ActivityBookInfoBinding.inflate(layoutInflater) }
    private lateinit var bookDetailItem: BookDetailItem
    private var bookId = -1
    private val token by lazy { SharedPreferenceHelper.getAuthorizationToken(applicationContext)!! }
    private val tags = ArrayList<String>()
    private val tagsAdapter by lazy { LabelAdapter(applicationContext, tags) }

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
            tagsAdapter.textColor = getColor(R.color.nsl_green)
            tagsAdapter.startString = "#"

            CoroutineScope(Dispatchers.Main).launch {
                bookInit()

            }
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
                } else {
                    ivItemBookImage.visibility = View.GONE
                }

                tags.addAll(bookDetailItem.bookTagList)
                tagsAdapter.notifyDataSetChanged()
            }
        } else {
            showShortToast("책 정보를 불러올 수 없습니다 2")
        }

    }


    private suspend fun loadImage(imageId: Int) {

        val response = nslAPI.getBookImageCall(token, imageId).awaitResponse()

        if (response.isSuccessful) {
            binding.run {
                val image = response.body()!!
                ivItemBookImage.setImageBitmap(BitmapFactory.decodeStream(image.byteStream()))

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
                        true
                    }
                    R.id.menu_post_delete -> {
                        showShortToast("삭제")
                        true
                    }
                    else -> false
                }
            }
        }
    }

}