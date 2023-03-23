package lab.nsl.nsl_app.pages.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.nsl.nsl_app.adapters.LangAdapter
import lab.nsl.nsl_app.adapters.RepoCardAdapter
import lab.nsl.nsl_app.adapters.decoration.RecyclerViewVerticalGap
import lab.nsl.nsl_app.databinding.FragmentProjectBinding
import lab.nsl.nsl_app.models.LanguagesModel
import lab.nsl.nsl_app.models.RepoCardItem
import lab.nsl.nsl_app.utils.ParentFragment
import lab.nsl.nsl_app.utils.SecretConstants
import lab.nsl.nsl_app.utils.SharedPreferenceHelper
import lab.nsl.nsl_app.utils.Utils
import lab.nsl.nsl_app.utils.githubAPI.GithubAPI
import lab.nsl.nsl_app.utils.githubAPI.responseDTO.RepoListDTO
import lab.nsl.nsl_app.utils.nslAPI.NSLAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse


class ProjectFragment : ParentFragment() {
    private val binding by lazy { FragmentProjectBinding.inflate(layoutInflater) }
    private val githubAPI by lazy { GithubAPI.create() }
    private val nslAPI by lazy { NSLAPI.create() }
    private val accountName = GithubAPI.githubAccountName
    private val githubAccessToken by lazy { SecretConstants.SECRET_GITHUB_TOKEN }
    private val nslAccessToken by lazy { SharedPreferenceHelper.getAuthorizationToken(requireContext()) }
    private lateinit var repoAdapter: RepoCardAdapter
    private var repoCardItemList = ArrayList<RepoCardItem>()

    private val langList by lazy { ArrayList<LanguagesModel>() }
    private val langAdapter by lazy { LangAdapter(requireContext(), langList) }

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
        binding.rvLang.adapter = langAdapter
        repoAdapter.notifyDataSetChanged()
        val getReposCall = githubAPI.getRepos(githubAccessToken, accountName)
        getReposCall.enqueue(object : Callback<RepoListDTO> {
            override fun onResponse(call: Call<RepoListDTO>, response: Response<RepoListDTO>) {
                hideProgress()

                if (response.isSuccessful) {
                    val body = response.body() as RepoListDTO
                    repoCardItemList.clear()

                    body.sortedByDescending { it.created_at }.forEach {
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



        CoroutineScope(Dispatchers.Main).launch {
            getLanguagesRate()
        }



        return binding.root
    }

    private suspend fun getLanguagesRate() {
        val call = nslAPI.getOrganizationLanguagesCall(nslAccessToken!!).awaitResponse()

        if (call.isSuccessful) {
            val body = call.body()!!
            body.forEach {
                langList.add(LanguagesModel(it.languageName, it.languageRate))
            }

            langAdapter.notifyDataSetChanged()
        } else {
            showShortToast("언어 비율을 가져오는데 실패했습니다")
            Log.d("devvv", call.errorBody()!!.string())
            Log.d("devvv", call.body().toString())
            Log.d("devvv", call.errorBody()!!.string())
        }
    }
}