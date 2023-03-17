package com.example.nsl_app.pages.book

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.adapters.BookAdapter
import com.example.nsl_app.databinding.ActivityBookListBinding
import com.example.nsl_app.models.BookItem
import com.example.nsl_app.utils.Constants
import com.example.nsl_app.utils.ParentActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.example.nsl_app.utils.nslAPI.NSLAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class BookListActivity : ParentActivity() {
    private val token by lazy { SharedPreferenceHelper.getAuthorizationToken(applicationContext)!! }
    private val nslAPI by lazy { NSLAPI.create() }
    private val binding by lazy { ActivityBookListBinding.inflate(layoutInflater) }
    private val bookList = ArrayList<BookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBook)
        supportActionBar?.run {
            // 앱 바 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }

        val bookAdapter = BookAdapter(this, bookList)
        bookAdapter.itemSelectListener = object : BookAdapter.ItemSelectListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@BookListActivity, BookInfoActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_BOOK_ID, bookList[position].bookId)
                startActivity(intent)
            }
        }

        binding.run {
            listBook.adapter = bookAdapter
            listBook.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            bookAdapter.notifyDataSetChanged()

            btnBookAdd.setOnClickListener {
                startActivity(Intent(this@BookListActivity, BookAddActivity::class.java))
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
            response.body()?.forEach {
                val bookItem = BookItem(it.id, it.bookName, it.bookAuthor, it.bookTagList)
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