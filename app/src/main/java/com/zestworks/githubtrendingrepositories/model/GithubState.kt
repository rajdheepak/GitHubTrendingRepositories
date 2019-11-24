package com.zestworks.githubtrendingrepositories.model

data class GithubState (
    val gitHubApiResponseState: GitHubApiResponseState = YetToStart,
    val gitHubApiResponse: List<GitHubApiResponse> = mutableListOf()
)

sealed class GitHubApiResponseState
object YetToStart : GitHubApiResponseState()
object RequestOnGoing : GitHubApiResponseState()
object Success : GitHubApiResponseState()
object RequestFailed : GitHubApiResponseState()