package com.zestworks.githubtrendingrepositories.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import kotlinx.android.synthetic.main.github_list_fragment.view.*

class GithubListFragment: Fragment() {

    private lateinit var gitHubViewModel: GitHubViewModel
    private lateinit var compositeDisposable: CompositeDisposable
    private var gitHubApiResponse = listOf<GitHubApiResponse>()

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.github_list_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.listing_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.sort_by_stars -> {
                gitHubViewModel.actionSwitchToStars()
            }
            R.id.sort_by_name -> {
                gitHubViewModel.actionSwitchToName()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_layout.visibility = View.VISIBLE
        shimmer_layout.startShimmer()
        error_page_layout.visibility = View.GONE
        github_list_view.layoutManager = LinearLayoutManager(context)
        github_list_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        github_list_view.adapter = GithubListAdapter(arrayListOf(), object: Listener {
            override fun onTouch(position: Int) {
                gitHubViewModel.onTouchingViewHolder(position)
            }

        })
        swipe_refresh.setOnRefreshListener {
            gitHubViewModel.fetchGithubRepositories()
        }
        retry_button.setOnClickListener {
            gitHubViewModel.fetchGithubRepositories()
        }
       /* toolbar.sort_by_stars_button.setOnClickListener {
            gitHubViewModel.actionSwitchToStars()
        }*/
    }

    @SuppressLint("CheckResult")
    override fun onStart() {
        super.onStart()
        gitHubViewModel = ViewModelFactory.getGithubViewModel(activity!! as AppCompatActivity)
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(gitHubViewModel.githubState.observeOn(AndroidSchedulers.mainThread()).subscribe {
            (github_list_view.adapter as GithubListAdapter).updatePosition(it.touchedItem)
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
            if(it.listingType == ListingType.DEFAULT) {
                gitHubViewModel.getGithubsByStars().removeObservers(this)
                gitHubViewModel.getGitHubByNames().removeObservers(this)
                if(!gitHubViewModel.getGitHubs().hasActiveObservers()) {
                    gitHubViewModel.getGitHubs().observe(this, Observer {
                        gitHubApiResponse = it
                        populateUi(it)
                    })
                }
            } else if (it.listingType == ListingType.SORT_BY_STARS){
                gitHubViewModel.getGitHubs().removeObservers(this)
                gitHubViewModel.getGitHubByNames().removeObservers(this)
                if(!gitHubViewModel.getGithubsByStars().hasActiveObservers()) {
                    gitHubViewModel.getGithubsByStars().observe(this, Observer {
                        gitHubApiResponse = it
                        populateUi(it)
                    })
                }
            } else {
                gitHubViewModel.getGitHubs().removeObservers(this)
                gitHubViewModel.getGithubsByStars().removeObservers(this)
                if(!gitHubViewModel.getGitHubByNames().hasActiveObservers()) {
                    gitHubViewModel.getGitHubByNames().observe(this, Observer {
                        gitHubApiResponse = it
                        populateUi(it)
                    })
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

    private fun populateUi(it: List<GitHubApiResponse>) {
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)?.isRefreshing =
            false
        if (it.isNotEmpty()) {
            if (shimmer_layout.visibility == View.VISIBLE) {
                shimmer_layout.visibility = View.GONE
                shimmer_layout.stopShimmer()
            }
            github_list_view.visibility = View.VISIBLE
            error_page_layout.visibility = View.GONE
            (github_list_view.adapter as GithubListAdapter).updateGithubItems(it)
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

}