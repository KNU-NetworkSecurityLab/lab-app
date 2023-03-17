package com.example.nsl_app.pages.book

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookEditActivity : BookAddActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commonInit()
        bookEditUpload()

        binding.btnBookAddFinish.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                bookEdit()
            }
        }
    }

    private fun bookEdit() {

    }

    private fun bookEditUpload() {

    }

}