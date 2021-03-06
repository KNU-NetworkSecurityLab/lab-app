package com.example.nsl_app.pages.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.adapters.MemberAdapter
import com.example.nsl_app.databinding.ActivityMemberBinding
import com.example.nsl_app.models.MemberItem
import com.example.nsl_app.utils.SecretConstants
import com.example.nsl_app.utils.notionAPI.NotionAPI
import com.example.nsl_app.utils.notionAPI.NotionMemberResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMemberBinding.inflate(layoutInflater) }
    private val notionAPI by lazy { NotionAPI.create() }
    private val notionToken by lazy { SecretConstants.SECRET_NOTION_TOKEN }
    private val members = ArrayList<MemberItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        toolbarSetUp()

        val memberAdapter =  MemberAdapter(this, members)

        binding.run {
            rvMember.adapter = memberAdapter
            rvMember.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        }

        val getMemberCall = notionAPI.getMember(NotionAPI.NOTION_MEMBER_DB_ID, NotionAPI.NOTION_API_VERSION, notionToken)

        getMemberCall.enqueue(object : Callback<NotionMemberResponse> {
            override fun onResponse(
                call: Call<NotionMemberResponse>,
                response: Response<NotionMemberResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    members.clear()

                    body.results.forEach {
                        val email = it.properties.Email.email ?: ""

                        val skills = ArrayList<String>()

                        it.properties.??????.multi_select.forEach {
                            skills.add(it.name)
                        }

                        members.add(MemberItem(
                            it.properties.??????.title[0].text.content,
                            it.properties.??????.number.toString(),
                            it.properties.??????.select.name,
                            email,
                            skills))
                    }

                    memberAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NotionMemberResponse>, t: Throwable) {

            }
        })

    }


    private fun toolbarSetUp() {
        setSupportActionBar(binding.toolbarMember)
        supportActionBar?.run {
            // ??? ??? ???????????? ?????? ??????
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // ??? ??? ?????? ?????????
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}