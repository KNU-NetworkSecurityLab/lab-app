package com.example.nsl_app.pages.community

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.databinding.FragmentCommunityBinding
import com.example.nsl_app.utils.Utils
import com.example.nsl_app.utils.githubAPI.GithubAPI
import com.example.nsl_app.utils.githubAPI.responseDTO.ReadMeDTO
import com.example.nsl_app.utils.githubAPI.responseDTO.RepoListDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityFragment : Fragment() {
    private val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }
    private val githubAPI by lazy { GithubAPI.create() }
    private val accountName = GithubAPI.githubAccountName
    private lateinit var repoAdapter:RepoCardAdapter
    private var repoCardItemList = ArrayList<RepoCardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.rvRepos.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        val tags = ArrayList<String>()
        tags.add("Java")
        tags.add("Kotlin")
        repoCardItemList.add(RepoCardItem("BoT",tags,"블록체인 기반 IoT 고도화 시스템"))
        repoCardItemList.add(RepoCardItem("BoT",tags,"블록체인 기반 IoT 고도화 시스템"))
        repoCardItemList.add(RepoCardItem("BoT",tags,"블록체인 기반 IoT 고도화 시스템"))


        repoAdapter = RepoCardAdapter(requireContext(), repoCardItemList)
        binding.rvRepos.adapter = repoAdapter
        repoAdapter.notifyDataSetChanged()

        val getReposCall = githubAPI.getRepos(accountName)
        getReposCall.enqueue(object : Callback<RepoListDTO> {
            override fun onResponse(call: Call<RepoListDTO>, response: Response<RepoListDTO>) {
                if (response.isSuccessful) {
                    val body = response.body() as RepoListDTO

                    body.forEach {
                        val repoName = it.name

                        val getReadMeCall = githubAPI.getReadMe(accountName, repoName)
                        getReadMeCall.enqueue(object : Callback<ReadMeDTO> {
                            override fun onResponse(call: Call<ReadMeDTO>, response: Response<ReadMeDTO>) {
                                if(response.isSuccessful) {
                                    val readMeBody = response.body() as ReadMeDTO
                                    //binding.tvReadMe.text = "${binding.tvReadMe.text}\n${repoName}\n${Utils.getBase64Decode(readMeBody.content)}"

                                    Log.d("${readMeBody.name} ReadMe", Utils.getBase64Decode(readMeBody.content))
                                }
                            }

                            override fun onFailure(call: Call<ReadMeDTO>, t: Throwable) {

                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<RepoListDTO>, t: Throwable) {

            }
        })


        return binding.root
    }

}