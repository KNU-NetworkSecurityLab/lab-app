package com.example.nsl_app.pages.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nsl_app.adapters.RepoCardAdapter
import com.example.nsl_app.adapters.decoration.RecyclerViewVerticalGap
import com.example.nsl_app.databinding.FragmentProjectBinding
import com.example.nsl_app.models.RepoCardItem
import com.example.nsl_app.utils.ParentFragment
import com.example.nsl_app.utils.Utils
import com.example.nsl_app.utils.githubAPI.GithubAPI
import com.example.nsl_app.utils.githubAPI.responseDTO.RepoListDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProjectFragment : ParentFragment() {
    private val binding by lazy { FragmentProjectBinding.inflate(layoutInflater) }
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

        showProgress(requireActivity(), Utils.getLoadingMessage())

        binding.rvRepos.addItemDecoration(RecyclerViewVerticalGap(40))

        repoAdapter = RepoCardAdapter(
            requireContext(),
            Utils.getDisplayWidthHeight(requireActivity())[0],
            repoCardItemList
        )
        binding.rvRepos.adapter = repoAdapter
        repoAdapter.notifyDataSetChanged()

        val getReposCall = githubAPI.getRepos(accountName)
        getReposCall.enqueue(object : Callback<RepoListDTO> {
            override fun onResponse(call: Call<RepoListDTO>, response: Response<RepoListDTO>) {
                hideProgress()

                if (response.isSuccessful) {
                    val body = response.body() as RepoListDTO
                    repoCardItemList.clear()

                    body.forEach {
                        val tags = ArrayList<String>()
                        if (it.language != null) tags.add(it.language) else tags.add("")
                        val description = it.description ?: ""
                        repoCardItemList.add(RepoCardItem(it.name, tags, description))

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