package com.zestworks.githubtrendingrepositories.model

data class GithubState (
    val gitHubApiResponseState: GitHubApiResponseState = YetToStart,
    val touchedItem: Int = -1
)

sealed class GitHubApiResponseState
object YetToStart : GitHubApiResponseState()
object RequestOnGoing : GitHubApiResponseState()
object Success : GitHubApiResponseState()
object RequestFailed : GitHubApiResponseState()