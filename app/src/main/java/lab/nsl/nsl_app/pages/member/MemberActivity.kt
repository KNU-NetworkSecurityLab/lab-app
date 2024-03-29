package lab.nsl.nsl_app.pages.member

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lab.nsl.nsl_app.adapters.MemberAdapter
import lab.nsl.nsl_app.adapters.decoration.RecyclerViewVerticalGap
import lab.nsl.nsl_app.databinding.ActivityMemberBinding
import lab.nsl.nsl_app.models.MemberItem
import lab.nsl.nsl_app.utils.ParentActivity
import lab.nsl.nsl_app.utils.SecretConstants
import lab.nsl.nsl_app.utils.Utils
import lab.nsl.nsl_app.utils.notionAPI.NotionAPI
import lab.nsl.nsl_app.utils.notionAPI.NotionMemberResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberActivity : ParentActivity() {
    private val binding by lazy { ActivityMemberBinding.inflate(layoutInflater) }
    private val notionAPI by lazy { NotionAPI.create() }
    private val notionToken by lazy { SecretConstants.SECRET_NOTION_TOKEN }
    private val members = ArrayList<MemberItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        toolbarSetUp()

        val memberAdapter = MemberAdapter(this, members)

        binding.run {
            rvMember.adapter = memberAdapter
            rvMember.layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            rvMember.addItemDecoration(RecyclerViewVerticalGap(50))
        }

        val getMemberCall = notionAPI.getMember(
            NotionAPI.NOTION_MEMBER_DB_ID,
            NotionAPI.NOTION_API_VERSION,
            notionToken
        )

        showProgress(this@MemberActivity, Utils.getLoadingMessage())

        getMemberCall.enqueue(object : Callback<NotionMemberResponse> {
            override fun onResponse(
                call: Call<NotionMemberResponse>,
                response: Response<NotionMemberResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    members.clear()

                    val sortCriteria = arrayOf("랩장", "부원", "휴학생", "졸업생")

                    body.results.sortedWith(
                        compareBy(
                            { sortCriteria.indexOf(it.properties.구분.select.name) }, // 정렬 기준 1. 랩장 -> 부원 -> 휴학생 -> 졸업생 순으로 정렬
                            { -it.properties.학번.number }) // 정렬 기준 2. 저학번 순 (내림차순)
                    ).forEach {
                        val email = it.properties.Email.email ?: ""

                        val skills = ArrayList<String>()

                        it.properties.스택.multi_select.forEach {
                            skills.add(it.name)
                        }

                        members.add(
                            MemberItem(
                                it.properties.이름.title[0].text.content,
                                it.properties.학번.number.toString(),
                                it.properties.구분.select.name,
                                email,
                                skills
                            )
                        )
                    }
                    hideProgress()
                    memberAdapter.notifyDataSetChanged()
                } else {
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<NotionMemberResponse>, t: Throwable) {
                hideProgress()
            }
        })

    }


    private fun toolbarSetUp() {
        setSupportActionBar(binding.toolbarMember)
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