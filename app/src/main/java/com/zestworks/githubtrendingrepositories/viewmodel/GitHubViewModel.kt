package com.zestworks.githubtrendingrepositories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.zestworks.githubtrendingrepositories.model.*
import com.zestworks.githubtrendingrepositories.repository.GitHubRepository
import com.zestworks.githubtrendingrepositories.repository.ResponseListener

class GitHubViewModel(val gitHubRepository: GitHubRepository): ViewModel() {

    val githubState: BehaviorRelay<GithubState> = BehaviorRelay.create()

    init {
        githubState.accept(GithubState())
    }

    fun fetchGithubRepositories() {
        if(githubState.value.gitHubApiResponseState !is RequestOnGoing) {
            githubState.accept(githubState.value.copy(gitHubApiResponseState = RequestOnGoing))
            gitHubRepository.fetchGitHubRepositories(object : ResponseListener {
                override fun onSuccess() {
                    githubState.accept(githubState.value.copy(gitHubApiResponseState = Success))
                }

                override fun onFailure() {
                    githubState.accept(githubState.value.copy(gitHubApiResponseState = RequestFailed))
                }

            })
        }
    }

    fun getGitHubs(): LiveData<List<GitHubApiResponse>> {
        return gitHubRepository.getGitHubs()
    }

}