package com.zestworks.githubtrendingrepositories.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.zestworks.githubtrendingrepositories.R
import com.zestworks.githubtrendingrepositories.model.*
import com.zestworks.githubtrendingrepositories.viewmodel.GitHubViewModel
import com.zestworks.githubtrendingrepositories.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.error_page_layout.*
import kotlinx.android.synthetic.main.github_list_fragment.*

class GithubListFragment: Fragment() {

    private lateinit var gitHubViewModel: GitHubViewModel
    private lateinit var compositeDisposable: CompositeDisposable
    private var gitHubApiResponse = listOf<GitHubApiResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.github_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_layout.visibility = View.VISIBLE
        shimmer_layout.startShimmer()
        error_page_layout.visibility = View.GONE
        github_list_view.layoutManager = LinearLayoutManager(context)
        github_list_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        github_list_view.adapter = GithubListAdapter(arrayListOf())
        swipe_refresh.setOnRefreshListener {
            gitHubViewModel.fetchGithubRepositories()
        }
        retry_button.setOnClickListener {
            gitHubViewModel.fetchGithubRepositories()
        }
    }

    @SuppressLint("CheckResult")
    override fun onStart() {
        super.onStart()
        gitHubViewModel = ViewModelFactory.getGithubViewModel(activity!! as AppCompatActivity)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(gitHubViewModel.githubState.observeOn(AndroidSchedulers.mainThread()).subscribe {
            when(it.gitHubApiResponseState) {
                YetToStart -> {
                    if(shimmer_layout.visibility != View.VISIBLE && gitHubApiResponse.isEmpty()) {
                        error_page_layout.visibility = View.GONE
                        shimmer_layout.visibility = View.VISIBLE
                        shimmer_layout.startShimmer()
                    }
                }
                RequestOnGoing -> {
                    if(shimmer_layout.visibility != View.VISIBLE && gitHubApiResponse.isEmpty()) {
                        error_page_layout.visibility = View.GONE
                        shimmer_layout.visibility = View.VISIBLE
                        shimmer_layout.startShimmer()
                    }
                }
                Success -> {
                    swipe_refresh.isRefreshing = false
                }
                RequestFailed -> {
                    swipe_refresh.isRefreshing = false
                    if(gitHubApiResponse.isNotEmpty()) {
                        Snackbar.make(view!!,"Something went wrong",Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(view!!,"Something went wrong",Snackbar.LENGTH_SHORT).show()
                        shimmer_layout.visibility = View.GONE
                        shimmer_layout.stopShimmer()
                        github_list_view.visibility = View.GONE
                        error_page_layout.visibility = View.VISIBLE
                    }
                }
            }
        })
        gitHubViewModel.getGitHubs().observe(this, Observer {
            gitHubApiResponse = it
            view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)?.isRefreshing = false
            if(it.isNotEmpty()) {
                if(shimmer_layout.visibility == View.VISIBLE) {
                    shimmer_layout.visibility = View.GONE
                    shimmer_layout.stopShimmer()
                }
                github_list_view.visibility = View.VISIBLE
                error_page_layout.visibility = View.GONE
                (github_list_view.adapter as GithubListAdapter).updateGithubItems(it)
            }
        })
        gitHubViewModel.fetchGithubRepositories()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}