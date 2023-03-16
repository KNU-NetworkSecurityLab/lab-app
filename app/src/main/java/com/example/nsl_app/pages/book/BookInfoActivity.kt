package com.example.nsl_app.pages.book

import android.os.Bundle
import android.view.MenuItem
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

                tags.addAll(bookDetailItem.bookTagList)
                tagsAdapter.notifyDataSetChanged()
            }
        } else {
            showShortToast("책 정보를 불러올 수 없습니다 2")
        }

    }


    private fun toolbarSetUp() {
        setSupportActionBar(binding.toolbarBookInfo)
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