package com.zestworks.githubtrendingrepositories.repository

import androidx.lifecycle.LiveData
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse

interface GitHubRepository {
    fun fetchGitHubRepositories(responseListener: ResponseListener)
    fun getGitHubs(): LiveData<List<GitHubApiResponse>>
    fun getGitHubsByStars(): LiveData<List<GitHubApiResponse>>
    fun getGithubByName(): LiveData<List<GitHubApiResponse>>
}

interface ResponseListener {
    fun onSuccess()
    fun onFailure()
}