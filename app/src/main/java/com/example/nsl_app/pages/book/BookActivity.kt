package com.example.nsl_app.pages.book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.adapters.BookAdapter
import com.example.nsl_app.databinding.ActivityBookBinding
import com.example.nsl_app.models.BookItem
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class BookActivity : ParentActivity() {
    private val token by lazy { SharedPreferenceHelper.getAuthorizationToken(applicationContext)!! }
    private val nslAPI by lazy { NSLAPI.create() }
    private val binding by lazy { ActivityBookBinding.inflate(layoutInflater) }
    private val bookList = ArrayList<BookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBook)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        val bookList = ArrayList<BookItem>()
        val tempTags = ArrayList<String>()

        tempTags.add("Java")
        tempTags.add("안드로이드")

        bookList.add(BookItem(1, "Do it! 안드로이드", "강XX", tempTags))
        bookList.add(BookItem(2, "월급 두배로 받는 법", "전XX", tempTags))
        bookList.add(BookItem(3, "학점 All A+ 받는 법", "성XX", tempTags))

        val bookAdapter = BookAdapter(this, bookList)

        binding.run {
            listBook.adapter = bookAdapter
            listBook.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            bookAdapter.notifyDataSetChanged()

            btnBookAdd.setOnClickListener {
                startActivity(Intent(this@BookActivity, BookAddActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.Main).launch {
            getBookList()
        }
    }

    private suspend fun getBookList() {
        bookList.clear()

        val response = nslAPI.getBookListCall(token).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.bookList?.forEach {
                val bookTagsSample = arrayListOf("자바", "코틀린", "안드로이드")
                val bookItem = BookItem(it.bookId, it.bookName, it.bookAuthor, bookTagsSample)
                bookList.add(bookItem)
            }
            binding.listBook.adapter?.notifyDataSetChanged()
        } else {
            showShortToast("책 목록을 불러오는데 실패했습니다.")
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