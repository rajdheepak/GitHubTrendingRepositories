package com.zestworks.githubtrendingrepositories.viewmodel

import androidx.lifecycle.ViewModel
import com.zestworks.githubtrendingrepositories.repository.GitHubRepository
import com.zestworks.githubtrendingrepositories.repository.ResponseListener

class GitHubViewModel(val gitHubRepository: GitHubRepository): ViewModel() {

    fun fetchGithubRepositories() {
        gitHubRepository.fetchGitHubRepositories(object: ResponseListener {
            override fun onSuccess() {

            }

            override fun onFailure() {

            }

        })
    }

}