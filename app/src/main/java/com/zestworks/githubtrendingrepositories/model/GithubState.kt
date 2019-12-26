package com.zestworks.githubtrendingrepositories.model

data class GithubState (
    val gitHubApiResponseState: GitHubApiResponseState = YetToStart,
    val touchedItem: Int = -1,
    val listingType: ListingType = ListingType.DEFAULT,
    var apiList: List<GitHubApiResponse> = listOf()
)

sealed class GitHubApiResponseState
object YetToStart : GitHubApiResponseState()
object RequestOnGoing : GitHubApiResponseState()
object Success : GitHubApiResponseState()
object RequestFailed : GitHubApiResponseState()
enum class ListingType {
    DEFAULT, SORT_BY_STARS, SORT_BY_NAME
}