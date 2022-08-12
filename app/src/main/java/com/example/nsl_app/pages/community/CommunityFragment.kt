package com.example.nsl_app.pages.community

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.adapters.RepoCardAdapter
import com.example.nsl_app.databinding.FragmentCommunityBinding
import com.example.nsl_app.models.RepoCardItem
import com.example.nsl_app.utils.Utils
import com.example.nsl_app.utils.githubAPI.GithubAPI
import com.example.nsl_app.utils.githubAPI.responseDTO.RepoListDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityFragment : Fragment() {
    private val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }
    private val githubAPI by lazy { GithubAPI.create() }
    private val accountName = GithubAPI.githubAccountName
    private lateinit var repoAdapter: RepoCardAdapter
    private var repoCardItemList = ArrayList<RepoCardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.rvRepos.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        repoAdapter = RepoCardAdapter(requireContext(), Utils.getDisplayWidthHeight(requireActivity())[0], repoCardItemList)
        binding.rvRepos.adapter = repoAdapter
        repoAdapter.notifyDataSetChanged()

        val getReposCall = githubAPI.getRepos(accountName)
        getReposCall.enqueue(object : Callback<RepoListDTO> {
            override fun onResponse(call: Call<RepoListDTO>, response: Response<RepoListDTO>) {
                if (response.isSuccessful) {
                    val body = response.body() as RepoListDTO
                    repoCardItemList.clear()

                    body.forEach {
                        val tags = ArrayList<String>()
                        if (it.language != null) tags.add(it.language) else tags.add("")
                        val description = it.description ?: ""
                        // TODO API 가 가장 많이 쓰인 언어 1개만 보여주므로, 모든 언어를 표시하려면 별도의 API 요청 필요.
                        repoCardItemList.add(RepoCardItem(it.name, tags, description))

//                        val repoName = it.name
//
//                        val getReadMeCall = githubAPI.getReadMe(accountName, repoName)
//                        getReadMeCall.enqueue(object : Callback<ReadMeDTO> {
//                            override fun onResponse(call: Call<ReadMeDTO>, response: Response<ReadMeDTO>) {
//                                if(response.isSuccessful) {
//                                    val readMeBody = response.body() as ReadMeDTO
//
//
//
//                                    //binding.tvReadMe.text = "${binding.tvReadMe.text}\n${repoName}\n${Utils.getBase64Decode(readMeBody.content)}"
//
//                                    Log.d("${readMeBody.name} ReadMe", Utils.getBase64Decode(readMeBody.content))
//                                }
//                            }
//
//                            override fun onFailure(call: Call<ReadMeDTO>, t: Throwable) {
//
//                            }
//                        })
                    }
                    repoAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<RepoListDTO>, t: Throwable) {

            }
        })


        return binding.root
    }

}