package com.zestworks.githubtrendingrepositories.repository

interface GitHubRepository {
    fun fetchGitHubRepositories(responseListener: ResponseListener)
}

interface ResponseListener {
    fun onSuccess()
    fun onFailure()
}