package com.example.nsl_app.pages.home.bookPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.databinding.ActivityBookBinding
import com.example.nsl_app.pages.home.bookPage.bookAdd.BookAddActivity

class BookActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBookBinding.inflate(layoutInflater) }

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

        bookList.add(BookItem("Do it! 안드로이드", "강XX", tempTags))
        bookList.add(BookItem("월급 두배로 받는 법", "전XX", tempTags))
        bookList.add(BookItem("학점 All A+ 받는 법", "성XX", tempTags))

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