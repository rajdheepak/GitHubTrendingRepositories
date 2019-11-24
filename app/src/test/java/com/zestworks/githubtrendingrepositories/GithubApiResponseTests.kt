package com.zestworks.githubtrendingrepositories

import com.zestworks.githubtrendingrepositories.repository.GitHubRequestMaker
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GithubApiResponseTests: MockGithubApi<GitHubRequestMaker>() {
    private lateinit var gitHubRequestMaker: GitHubRequestMaker

    @Before
    fun init() {
        gitHubRequestMaker = createService(GitHubRequestMaker::class.java)
    }

    @Test
    fun fetchPostTests() {
        enqueueResponse("apiresponse.json")
        val fetchWeatherForecast = gitHubRequestMaker.fetchWeatherForecast().blockingFirst()
        Assert.assertEquals(25, fetchWeatherForecast.body()?.size)
    }
}